package money.terra.amino.serializer.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kr.jadekim.common.encoder.decodeBase64
import kr.jadekim.common.encoder.encodeBase64
import money.terra.type.Binary

object Base64BinarySerializer : KSerializer<Binary> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Base64Binary", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Binary) {
        encoder.encodeString(value.data.encodeBase64())
    }

    override fun deserialize(decoder: Decoder): Binary = Binary(decoder.decodeString().decodeBase64())
}