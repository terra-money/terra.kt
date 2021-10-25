package money.terra.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import money.terra.TERRA_DECIMAL_SCALE

@Serializable(DecimalSerializer::class)
expect class Decimal(value: String) : Number, Comparable<Decimal> {

    companion object {
        val ZERO: Decimal
        val ONE: Decimal
    }

    operator fun plus(other: Decimal): Decimal

    operator fun minus(other: Decimal): Decimal

    operator fun times(other: Decimal): Decimal

    operator fun div(other: Decimal): Decimal

    operator fun rem(other: Decimal): Decimal
}

expect operator fun Decimal.times(other: Uint128): Decimal

expect fun Decimal.ceil(): Decimal

expect fun Decimal.toUint128(): Uint128

expect fun Decimal.unscaled(scale: Int = TERRA_DECIMAL_SCALE): Uint128

fun ULong.toDecimal(): Decimal = Decimal(toString())

object DecimalSerializer : KSerializer<Decimal> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Decimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Decimal) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Decimal = Decimal(decoder.decodeString())
}
