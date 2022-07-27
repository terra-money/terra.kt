package money.terra.proto.mapper

import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import com.google.protobuf.Parser
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import money.terra.proto.serializer.ProtobufSerializer

interface ProtobufTypeMapper<M, P : Message> {

    val descriptor: Descriptors.Descriptor

    val parser: Parser<P>

    fun convert(obj: M): P

    fun convert(obj: P): M

    fun M.toProtobuf(): P = convert(this)

    fun P.fromProtobuf(): M = convert(this)

    fun serialize(obj: M): ByteArray = obj.toProtobuf().toByteArray()

    fun deserialize(bytes: ByteArray): M = parser.parseFrom(bytes).fromProtobuf()

    fun M.toByteArray(): ByteArray = serialize(this)
}

fun <M> ByteArray.parseProtobuf(mapper: ProtobufTypeMapper<M, *>): M = mapper.deserialize(this)

inline fun <Base : Any, reified M : Base> PolymorphicModuleBuilder<Base>.mapper(mapper: ProtobufTypeMapper<M, *>) {
    subclass(M::class, ProtobufSerializer(mapper))
}
