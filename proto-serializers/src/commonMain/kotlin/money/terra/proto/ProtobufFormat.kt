package money.terra.proto

import com.google.protobuf.ByteString
import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import kotlinx.serialization.modules.plus
import money.terra.proto.serializer.message.MessageSerializersModule
import money.terra.proto.serializer.type.TypeSerializersModule

object ProtobufFormat : BinaryFormat {

    override val serializersModule: SerializersModule
        get() = _serializersModule

    @Suppress("ObjectPropertyName")
    private var _serializersModule: SerializersModule = TypeSerializersModule + MessageSerializersModule

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val encoder = ProtobufByteArrayEncoder(serializersModule)

        serializer.serialize(encoder, value)

        return encoder.result
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T : Any> encodeToByteArray(value: T): ByteArray {
        val serializer = serializersModule.getContextual(T::class)
            ?: serializersModule.serializer()

        return encodeToByteArray(serializer, value)
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        val decoder = ProtobufByteArrayDecoder(bytes, serializersModule)

        return deserializer.deserialize(decoder)
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T : Any> decodeFromByteArray(value: ByteArray): T {
        val serializer = serializersModule.getContextual(T::class)
            ?: serializersModule.serializer()

        return decodeFromByteArray(serializer, value)
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T : Any> encodeToAny(value: T): com.google.protobuf.Any {
        val serializer = serializersModule.getPolymorphic(T::class, value)
            ?: serializersModule.getContextual(T::class)
            ?: serializersModule.serializer()

        return com.google.protobuf.Any.newBuilder()
            .setTypeUrl(serializer.descriptor.serialName)
            .setValue(ByteString.copyFrom(encodeToByteArray(serializer, value)))
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T : Any> decodeFromAny(value: com.google.protobuf.Any): T {
        val serializer = serializersModule.getPolymorphic(T::class, value.typeUrl)
            ?: serializersModule.getContextual(T::class)
            ?: serializersModule.serializer()

        return decodeFromByteArray(serializer, value.value.toByteArray())
    }

    fun registerSerializersModule(module: SerializersModule, overwrite: Boolean = false) {
        _serializersModule = if (overwrite) {
            serializersModule.overwriteWith(module)
        } else {
            serializersModule + module
        }
    }
}
