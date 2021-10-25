package money.terra.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ULongAsStringSerializer : KSerializer<ULong> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ULongString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ULong) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ULong = decoder.decodeString().toULong()
}