package money.terra.type

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

@Serializable(DecimalSerializer::class)
actual class Decimal constructor(internal val origin: BigDecimal) : Number(), Comparable<Decimal> {

    actual companion object {
        actual val ZERO: Decimal = Decimal(BigDecimal.ZERO)
        actual val ONE: Decimal = Decimal(BigDecimal.ONE)
    }

    actual constructor(value: String) : this(BigDecimal(value))

    override fun compareTo(other: Decimal): Int = origin.compareTo(other.origin)

    override fun toByte(): Byte = origin.toByte()

    override fun toChar(): Char = origin.toChar()

    override fun toDouble(): Double = origin.toDouble()

    override fun toFloat(): Float = origin.toFloat()

    override fun toInt(): Int = origin.toInt()

    override fun toLong(): Long = origin.toLong()

    override fun toShort(): Short = origin.toShort()

    override fun toString(): String = origin.toPlainString()

    override fun equals(other: Any?): Boolean = origin.equals(other)

    override fun hashCode(): Int = origin.hashCode()

    actual operator fun plus(other: Decimal): Decimal = Decimal(origin + other.origin)

    actual operator fun minus(other: Decimal): Decimal = Decimal(origin - other.origin)

    actual operator fun times(other: Decimal): Decimal = Decimal(origin * other.origin)

    actual operator fun div(other: Decimal): Decimal = Decimal(origin / other.origin)

    actual operator fun rem(other: Decimal): Decimal = Decimal(origin % other.origin)
}

fun BigDecimal.asDecimal() = Decimal(this)

fun Decimal.asBigDecimal() = origin

actual operator fun Decimal.times(other: Uint128): Decimal = origin.multiply(other.origin.toBigDecimal()).asDecimal()

actual fun Decimal.ceil(): Decimal = origin.setScale(0, RoundingMode.CEILING).asDecimal()

actual fun Decimal.toUint128(): Uint128 = origin.toBigInteger().asUint128()

actual fun Decimal.unscaled(scale: Int): Uint128 = origin.setScale(scale).unscaledValue().asUint128()