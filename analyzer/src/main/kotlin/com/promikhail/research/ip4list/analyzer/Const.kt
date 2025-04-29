package com.promikhail.research.ip4list.analyzer

const val MB: Int = 1024 * 1024

const val CR: Byte = 13
const val LF: Byte = 10

/**
 * Размер буфера для итеративного чтения части файла.
 */
const val BUFFER_SIZE = 32 * 1024

/**
 * Размер строки содержащей IP адрес максимальной длины.
 * Нужно для чтения дополнительной строки на стыках частей файла.
 */
const val BUFFER_EXTRA_SIZE = 17