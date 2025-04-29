package com.promikhail.research.ip4list.generator.utils

fun Int.toIpAddress(): String {
    val byte1 = (this and 0xFF)
    val byte2 = (this shr 8) and 0xFF
    val byte3 = (this shr 16) and 0xFF
    val byte4 = (this shr 24) and 0xFF

    return "$byte4.$byte3.$byte2.$byte1"
}