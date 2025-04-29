package com.promikhail.research.ip4list.analyzer

import com.promikhail.research.ip4list.analyzer.fileChunk.FileChunk
import com.promikhail.research.ip4list.analyzer.utils.*
import com.promikhail.research.ip4list.tools.shardedArray.ICountArray
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.FileChannel
import kotlin.time.measureTimedValue


suspend fun processFileChunk(f: File, fileChunk: FileChunk, bm: ICountArray): Long {
    println(
        "PROCESS fileChunkNum=${fileChunk.num.toString().padStart(3)}, "
                + "startPos=${fileChunk.startPos.toString().padStart(16)}, "
                + "endPos=${fileChunk.endPos.toString().padStart(16)}, "
                + "thread=${Thread.currentThread().threadId()}"
    )

    val raf = RandomAccessFile(f, "r")
    val chan = raf.channel

    val timing = measureTimedValue {
        var linesRead = 0L

        // читаем все строки с текущего чанка файла
        readFileChunk(chan, fileChunk) { s ->
            // подсчет числа обработанных строк в чанке
            linesRead++

            // преобразуем IP-строку в числовое представление Int
            val ipInt = s.ipStrToInt()

            // отмечаем в битовом массиве, что такой IP встречался
            bm.incValue(ipInt)
        }
        linesRead
    }

    chan.close()
    raf.close()

    println(
        "PROCESS #${fileChunk.num.toString().padStart(3)} FINISHED! "
                + "Bytes processed: ${(fileChunk.endPos - fileChunk.startPos + 1).toString().padStart(16)}, "
                + "linesRead=${timing.value}, "
                + "elapsed=${timing.duration}"
    )

    return timing.value
}

suspend fun readFileChunk(channel: FileChannel, fileChunk: FileChunk, action: suspend (String) -> Unit) {
    if (fileChunk.num == 1) {
        channel.position(0)
    } else {
        // сдвигаем позицию курсора в канале на начало следующей строки
        // это нужно для согласования чтения строк на стыках чанков в файле
        // чтобы не было повторных чтений строк в разных потоках
        channel.moveChannelPositionToFirstNewLine(fileChunk.startPos - 1)
    }

    val buffer = ByteBuffer.allocate(BUFFER_SIZE)
    val bufferWithExtra = ByteBuffer.allocate(BUFFER_SIZE + BUFFER_EXTRA_SIZE)

    val ca = CharBuffer.allocate(20)

    while (true) {
        val channelStart = channel.position()

        if (channelStart + BUFFER_SIZE + 1 > fileChunk.endPos) {
            // ветка когда считывается последний блок в чанке
            // нужно для согласованного чтения полной строки в конце чанка

            bufferWithExtra.clear()
            val bytesRead = channel.read(bufferWithExtra)
            if (bytesRead == -1) break
            // перематываем курсор буфера в начало, лимит не меняем
            bufferWithExtra.rewind()

            // проставляем лимит для считанного буфера, до куда нужно читать его
            val searchStartPosition = (fileChunk.endPos - channelStart).toInt()
            var offset = bufferWithExtra.getFirstCrLfOffsetFromPosition(searchStartPosition)
            if (offset != -1) {
                bufferWithExtra.limit(offset)
            } else {
                bufferWithExtra.limit(searchStartPosition)
            }

            // читаем все строки из буфера и передаем в лямбду
            bufferWithExtra.readAllLinesOfBuffer(ca) {
                action(it)
            }
            break
        } else {
            // ветка когда считываются все блоки кроме последнего

            buffer.clear()
            val bytesRead = channel.read(buffer)
            if (bytesRead == -1) break
            // перематываем курсор буфера в начало, лимит не меняем
            buffer.rewind()

            // находим оффсет до последнего конца строки в буфере
            val offset = buffer.getLastCrLfOffsetToEnd()

            // сдвигаем курсор канала на этот оффсет
            // чтобы в след итерации читать с начала строки
            channel.position(channel.position() - offset)
            buffer.limit(bytesRead - offset)

            // читаем все строки из буфера и передаем в лямбду
            buffer.readAllLinesOfBuffer(ca) {
                action(it)
            }
        }
    }
}