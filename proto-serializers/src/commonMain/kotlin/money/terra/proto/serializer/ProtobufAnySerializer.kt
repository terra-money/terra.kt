package money.terra.proto.serializer

import com.google.protobuf.kotlin.toByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import money.terra.proto.ProtobufByteArrayEncoder
import money.terra.proto.ProtobufFormat
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
open class ProtobufAnySerializer<T : Any>(baseClass: KClass<T>) : AbstractPolymorphicSerializer<T>(baseClass) {

    companion object {
        private val byteArraySerializer = ByteArraySerializer()
    }

    override val descriptor: SerialDescriptor = RenamedSerialDescriptor(byteArraySerializer.descriptor, com.google.protobuf.Any.getDescriptor().fullName)

    override fun serialize(encoder: Encoder, value: T) {
        val actualSerializer = findPolymorphicSerializer(encoder, value)
        val result = com.google.protobuf.Any.newBuilder()
            .setTypeUrl(actualSerializer.descriptor.serialName)
            .setValue(ProtobufFormat.encodeToByteArray(actualSerializer, value).toByteString())
            .build()

        if (encoder is ProtobufByteArrayEncoder) {
            encoder.result = result.toByteArray()
        } else {
            byteArraySerializer.serialize(encoder, result.toByteArray())
        }
    }

    override fun deserialize(decoder: Decoder): T {
        val bytes = byteArraySerializer.deserialize(decoder)
        val any = com.google.protobuf.Any.parseFrom(bytes)
        val serializer = findPolymorphicSerializer(decoder, any.typeUrl)

        return ProtobufFormat.decodeFromByteArray(serializer, any.value.toByteArray())
    }
}
