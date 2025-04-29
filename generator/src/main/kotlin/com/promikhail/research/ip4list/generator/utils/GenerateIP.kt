package com.promikhail.research.ip4list.generator.utils

import kotlin.random.Random

/**
 * Создает случайный IP адрес и возвращает его числовое и строковое представление.
 */
fun genIpAddress(rnd: Random): Pair<Int, String> {
    val int = rnd.nextInt()
    return int to int.toIpAddress()
}

