package com.promikhail.research.ip4list.analyzer

import com.promikhail.research.ip4list.analyzer.utils.getFirstCrLfOffsetFromPosition
import com.promikhail.research.ip4list.analyzer.utils.getLastCrLfOffsetToEnd
import com.promikhail.research.ip4list.analyzer.utils.ipStrToInt
import com.promikhail.research.ip4list.analyzer.utils.readAllLinesOfBuffer
import kotlinx.coroutines.test.runTest
import java.nio.ByteBuffer
import java.nio.CharBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Test {

    fun stringToByteBuffer(s: String): ByteBuffer {
        return ByteBuffer.wrap(s.encodeToByteArray())
    }

    @Test
    fun testByteBuffer() = runTest {
        val ba = byteArrayOf(
            74, 8, 48, 3, 8,
            6, 4, 34, 16, 84,
            34, 65, 1, 56, 23
        )
        val bb = ByteBuffer.wrap(ba)
        bb.position(1)
        bb.limit(3)
        while (bb.hasRemaining()) {
            val b = bb.get()
            println(b)
        }
        assertEquals(0u, 0u)
    }

    @Test
    fun testReadAllLinesOfBuffer() = runTest {
        val s = "hello   \r\naloha  yo!\r\n\r\nDude wassup!!!\n How do you do??\r\nlast str"
        val buffer = stringToByteBuffer(s)

        val readLines = mutableListOf<String>()

        val ca = CharBuffer.allocate(17)

        buffer.readAllLinesOfBuffer(ca) { line ->
            readLines.add(line)
        }
        val referenceLines1 = listOf("hello   ", "aloha  yo!", "", "Dude wassup!!!", " How do you do??")
        assertContentEquals(referenceLines1, readLines)

        println()

        buffer.clear()
        buffer.limit(28)
        readLines.clear()

        buffer.readAllLinesOfBuffer(ca) { line ->
            readLines.add(line)
        }
        val referenceLines2 = listOf("hello   ", "aloha  yo!", "")
        assertContentEquals(referenceLines2, readLines)

    }

    @Test
    fun testGetFirstCrLfOffsetFromPosition() = runTest {
        val s = "hello   \r\naloha  yo!\r\n\r\nDude wassup!!!\n How do you do??\r\n\r\n"
        //       0           1          0

        val buffer = stringToByteBuffer(s)

        var cnt = buffer.getFirstCrLfOffsetFromPosition(0)

        assertEquals(10, cnt)
        assertEquals(0, buffer.position())

        cnt = buffer.getFirstCrLfOffsetFromPosition(23)

        assertEquals(24, cnt)
        assertEquals(0, buffer.position())

        cnt = buffer.getFirstCrLfOffsetFromPosition(24)

        assertEquals(39, cnt)
        assertEquals(0, buffer.position())
    }

    @Test
    fun testGetLastCrLfOffsetToEnd() = runTest {
        val s = "hello   \r\naloha  yo!\r\n\r\nDude wassup!!!\n How do you do??"
        val buffer = stringToByteBuffer(s)
        val cnt = buffer.getLastCrLfOffsetToEnd()
        assertEquals(16, cnt)
    }

    @Test
    fun testIStrToInt() {
        var i = "0.0.0.0".ipStrToInt()
        assertEquals(0, i)

        i = "255.255.255.255".ipStrToInt()
        assertEquals(-1, i)

        i = "255.255.255.254".ipStrToInt()
        assertEquals(-2, i)

        i = "10.10.10.10".ipStrToInt()
        assertEquals(168430090, i)

        i = "254.1.65.56".ipStrToInt()
        assertEquals(-33472200, i)
    }
}