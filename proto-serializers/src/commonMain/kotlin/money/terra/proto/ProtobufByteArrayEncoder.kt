package money.terra.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal class ProtobufByteArrayEncoder(override val serializersModule: SerializersModule) : AbstractEncoder() {

    var result: ByteArray = byteArrayOf()
        get() {
            if (field.isEmpty()) {
                field = bytes.toByteArray()
            }

            return field
        }

    private val bytes = mutableListOf<Byte>()

    override fun encodeValue(value: Any) {
        if (value is Byte) {
            bytes.add(value)
        }

        super.encodeValue(value)
    }
}