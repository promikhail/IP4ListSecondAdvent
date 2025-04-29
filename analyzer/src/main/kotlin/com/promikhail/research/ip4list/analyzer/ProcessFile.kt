package com.promikhail.research.ip4list.analyzer

import com.promikhail.research.ip4list.analyzer.fileChunk.createFileChunksList
import com.promikhail.research.ip4list.tools.shardedArray.ICountArray
import kotlinx.coroutines.*
import java.io.File
import kotlin.time.measureTimedValue

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun processFile(file: File, threadsCount: Int, countArray: ICountArray) {
    val fileSize = file.length()
    println("File size: $fileSize B, ${fileSize / MB} MB")

    // разбивка файла на чанки для параллельной обработки в разных потоках
    val fileChunks = createFileChunksList(file, threadsCount)

    val readResult = measureTimedValue {
        coroutineScope {
            val asyncJobs = mutableListOf<Deferred<Long>>()
            for (fileChunk in fileChunks) {
                val job = async(Dispatchers.IO) {
                    // запуск обработки одного чанка файла в отдельном потоке
                    processFileChunk(file, fileChunk, countArray)
                }
                // собираем все джобы в коллекцию, для дальнейщего сбора результатов
                asyncJobs.add(job)
            }
            // ждем заверщения работы всех корутин
            asyncJobs.awaitAll()
            // суммируем результаты всех корутин
            asyncJobs.sumOf { it.getCompleted() }
        }
    }

    println("Total elapsed to read: ${readResult.duration}")
    println("Total lines read: ${readResult.value}")

    // считаем количество уникальных адресов
    val countResult = countArray.getCount()

    println("Total unique IP address count: ${countResult.value}")
    println("Total elapsed to count: ${countResult.duration}")

    println("TOTAL time elapsed: ${readResult.duration.plus(countResult.duration).inWholeSeconds} sec")

}