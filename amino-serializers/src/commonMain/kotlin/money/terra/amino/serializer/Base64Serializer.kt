package money.terra.amino.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kr.jadekim.common.encoder.decodeBase64
import kr.jadekim.common.encoder.encodeBase64

object Base64Serializer : KSerializer<ByteArray> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Base64ByteArray", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(value.encodeBase64())
    }

    override fun deserialize(decoder: Decoder): ByteArray = decoder.decodeString().decodeBase64()
}