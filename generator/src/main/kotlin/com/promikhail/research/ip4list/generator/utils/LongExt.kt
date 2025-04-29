package com.promikhail.research.ip4list.generator.utils

fun Long.formatWithSeparator(separator: String = "_"): String {
    return this.toString()
        .reversed()
        .chunked(3)
        .joinToString(separator)
        .reversed()
}