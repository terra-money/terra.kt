package money.terra.amino.serializer.type

import kotlinx.serialization.modules.SerializersModule
import money.terra.amino.serializer.PolymorphicKeyValueSerializer
import money.terra.amino.serializer.PolymorphicObjectSerializer
import money.terra.model.EnumMessage
import money.terra.model.Message
import money.terra.type.Binary

val TypeSerializersModule = SerializersModule {
    contextual(Message::class, PolymorphicObjectSerializer(Message::class))
    contextual(EnumMessage::class, PolymorphicKeyValueSerializer(EnumMessage::class))
    contextual(Binary::class, Base64BinarySerializer)
}