package com.promikhail.research.ip4list.generator

import com.promikhail.research.ip4list.generator.utils.toIpAddress
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Test {

    @Test
    fun testIStrToInt() {
        var s = 0.toIpAddress()
        assertEquals("0.0.0.0", s)

        s = (-1).toIpAddress()
        assertEquals("255.255.255.255", s)

        s = (-2).toIpAddress()
        assertEquals("255.255.255.254", s)

        s = 168430090.toIpAddress()
        assertEquals("10.10.10.10", s)

        s = (-33472200).toIpAddress()
        assertEquals("254.1.65.56", s)
    }
}