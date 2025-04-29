package com.promikhail.research.ip4list.analyzer

import com.promikhail.research.ip4list.tools.shardedArray.BitSetArray
import kotlinx.coroutines.runBlocking
import java.io.File


fun main() = runBlocking {
    val fileName = "ip4list_1_000_000_000.txt"

    val file = File("files/$fileName")

    if (!file.exists()) {
        println("File not exists!")
        return@runBlocking
    }

    // запускаем столько параллельных потоков сколько есть ядер на машине
    val threadsCount = Runtime.getRuntime().availableProcessors()

    // инстанс массива для подсчета
    val bitSetArray = BitSetArray()

    // запуск обработки файла
    processFile(file, threadsCount, bitSetArray)
}