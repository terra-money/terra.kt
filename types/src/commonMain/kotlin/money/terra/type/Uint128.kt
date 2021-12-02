package money.terra.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import money.terra.TERRA_DECIMAL_SCALE

@Serializable(Uint128Serializer::class)
expect class Uint128(value: String) : Number, Comparable<Uint128> {

    companion object {
        val ZERO: Uint128
        val ONE: Uint128
    }

    operator fun plus(other: Uint128): Uint128

    operator fun minus(other: Uint128): Uint128

    operator fun times(other: Uint128): Uint128

    operator fun div(other: Uint128): Uint128

    operator fun rem(other: Uint128): Uint128
}

operator fun Uint128.times(other: Decimal): Decimal = other * this

expect fun Uint128.toDecimal(): Decimal

expect fun Uint128.scaled(scale: Int = TERRA_DECIMAL_SCALE): Decimal

fun Long.toUint128(): Uint128 = Uint128(toString())

fun ULong.toUint128(): Uint128 = Uint128(toString())

object Uint128Serializer : KSerializer<Uint128> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uint128", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uint128) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uint128 = Uint128(decoder.decodeString())
}
