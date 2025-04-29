package com.promikhail.research.ip4list.analyzer.utils


const val ZERO_CHAR_CODE = '0'.code

/**
 * Конвертирует строковое представление IP адреса в его числовое представление.
 */
fun String.ipStrToInt(): Int {
    var block = 0L
    var num = 0L

    var char: Char
    for (i in 0 until this.length) {
        char = this[i]
        if (char == '.') {
            block = (block shl 8) or num
            num = 0
        } else {
            num = num * 10 + char.code - ZERO_CHAR_CODE
        }
    }
    return ((block shl 8) or num).toInt()
}