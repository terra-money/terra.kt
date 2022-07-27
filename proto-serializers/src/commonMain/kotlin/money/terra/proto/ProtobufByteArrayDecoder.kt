package money.terra.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class ProtobufByteArrayDecoder(
    val bytes: ByteArray,
    override val serializersModule: SerializersModule,
) : AbstractDecoder() {

    private var index = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (bytes.size > index) 0 else -1

    override fun decodeValue(): Any = bytes[index]
}