package money.terra.type

import kotlinx.serialization.Contextual
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.math.pow

@kotlinx.serialization.Serializable
data class CompactBitArray(
    val extraBitsStored: Int,
    @Contextual val elements: Binary,
) {

    companion object {

        fun fromBits(bits: Int): CompactBitArray {
            if (bits <= 0) {
                throw IllegalArgumentException("CompactBitArray bits must be bigger than 0")
            }

            val numElements = (bits + 7) / 8
            if (numElements <= 0 || numElements > Int.MAX_VALUE) {
                throw IllegalArgumentException("CompactBitArray overflow")
            }

            return CompactBitArray(bits % 8, Binary(ByteArray(numElements)))
        }
    }

    val count: Int
        get() {
            if (extraBitsStored == 0) {
                return elements.data.size * 8
            }

            return (elements.data.size - 1) * 8 + extraBitsStored
        }

    fun getIndex(i: Int): Boolean {
        if (i < 0 || i>= count) {
            return false
        }

        return elements.data[i shr 3] and (1 shl (7 - (i % 8))).toByte() > 0
    }

    fun setIndex(i: Int, value: Boolean) : Boolean {
        if (i < 0 || i >= count) {
            return false
        }

        if (value) {
            elements.data[i shr 3] = elements.data[i shr 3] or (1 shl (7 - (i % 8))).toByte()
        } else {
            elements.data[i shr 3] = elements.data[i shr 3] and (1 shl (7 - (i % 8))).toByte().inv()
        }

        return true
    }

    fun numTrueBitsBefore(i: Int): Int {
        var index = i
        var onesCount = 0
        val max = count
        if (index > max) {
            index = max
        }

        repeat(elements.data.size) {
            if (it * 8 + 7 >= index) {
                onesCount += countOneBits(elements.data[it].toInt() shr (7 - (index % 8) + 1))
                return onesCount
            }
            onesCount += countOneBits(elements.data[it].toInt())
        }

        throw IllegalStateException("Unexpected result")
    }

    private fun countOneBits(number: Int): Int = number.toString(2).split('0').joinToString("").length
}