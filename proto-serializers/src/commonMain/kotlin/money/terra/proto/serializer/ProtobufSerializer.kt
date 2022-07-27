package money.terra.proto.serializer

import com.google.protobuf.Message
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import money.terra.proto.ProtobufByteArrayDecoder
import money.terra.proto.ProtobufByteArrayEncoder
import money.terra.proto.mapper.ProtobufTypeMapper

open class ProtobufSerializer<M, P : Message>(
    val mapper: ProtobufTypeMapper<M, P>,
    val typeUrl: String = "/" + mapper.descriptor.fullName,
) : KSerializer<M> {

    companion object {
        private val byteArraySerializer = ByteArraySerializer()
    }

    override val descriptor: SerialDescriptor = RenamedSerialDescriptor(byteArraySerializer.descriptor, typeUrl)

    override fun serialize(encoder: Encoder, value: M) {
        val result = mapper.serialize(value)

        if (encoder is ProtobufByteArrayEncoder) {
            encoder.result = result
        } else {
            byteArraySerializer.serialize(encoder, result)
        }
    }

    override fun deserialize(decoder: Decoder): M {
        val bytes = if (decoder is ProtobufByteArrayDecoder) {
            decoder.bytes
        } else {
            byteArraySerializer.deserialize(decoder)
        }

        return mapper.deserialize(bytes)
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal class RenamedSerialDescriptor(
    private val origin: SerialDescriptor,
    override val serialName: String,
) : SerialDescriptor by origin
