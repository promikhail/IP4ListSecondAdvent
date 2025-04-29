package com.promikhail.research.ip4list.tools.shardedArray

import kotlin.time.TimedValue

interface ICountArray {

    suspend fun incValue(value: Int)

    fun getCount(): TimedValue<Long>

}

