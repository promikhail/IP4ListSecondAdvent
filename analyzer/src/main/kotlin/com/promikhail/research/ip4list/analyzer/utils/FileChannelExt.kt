package com.promikhail.research.ip4list.analyzer.utils

import com.promikhail.research.ip4list.analyzer.BUFFER_EXTRA_SIZE
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Сдвигает текущую позицию в канале файла на первый конец строки после [searchStartPosition].
 */
fun FileChannel.moveChannelPositionToFirstNewLine(searchStartPosition: Long) {
    val buffer = ByteBuffer.allocate(BUFFER_EXTRA_SIZE)
    this.position(searchStartPosition)
    this.read(buffer)
    val offset = buffer.getFirstCrLfOffsetFromPosition(0)
    val newPosition = searchStartPosition + offset
    this.position(newPosition)
}