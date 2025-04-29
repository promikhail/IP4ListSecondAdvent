package com.promikhail.research.ip4list.generator


fun main() {
    val ipCount = 1_000_000_000L
    val isCrLf = false

    val generator = Generator()
    generator.generate(ipCount, isCrLf)
}