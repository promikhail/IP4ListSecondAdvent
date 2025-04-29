package com.promikhail.research.ip4list.tools.shardedArray

import java.util.*
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue


/**
 * Битовый массив для учета уникальных IP адресов.
 * В массив в элементе под индексом соответствующим Int числу IP адреса, проставляется единица.
 * Так как Int содержит 2^32 значений, то в один BitSet все не влезет,
 * поэтому отрицательную половину значений заносим во второй BitSet.
 */
class BitSetArray : ICountArray {
    // массив для положительных значений Int и ноля
    private val positive = BitSet(Int.MAX_VALUE)

    // массив для отрицательных значений Int
    private val negative = BitSet(Int.MAX_VALUE)

    // Ставим единицу в элемент соответствующего массива под индексом [value]
    override suspend fun incValue(value: Int) {
        if (value >= 0) {
            positive.set(value)
        } else {
            negative.set(value.inv()) // берем инвертированное значение отрицательного числа
        }
    }

    // Считаем количество единиц в массивах и суммируем. Замеряем время подсчета
    // Под капотом используется Long.bitCount, который считает количество битов со значением 1
    override fun getCount(): TimedValue<Long> {
        return measureTimedValue {
            positive.cardinality().toLong() + negative.cardinality()
        }
    }
}