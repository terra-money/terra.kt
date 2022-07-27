package money.terra.proto.serializer.type

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import money.terra.model.*
import money.terra.proto.mapper.mapper
import money.terra.proto.mapper.type.*
import money.terra.proto.serializer.ProtobufAnySerializer
import money.terra.proto.serializer.ProtobufSerializer
import money.terra.type.CompactBitArray

val TypeSerializersModule = SerializersModule {
    contextual(Message::class, ProtobufAnySerializer(Message::class))
    contextual(Coin::class, ProtobufSerializer(CoinMapper))
    contextual(Fee::class, ProtobufSerializer(FeeMapper))
    contextual(Transaction::class, ProtobufSerializer(TransactionMapper))
    contextual(TransactionBody::class, ProtobufSerializer(TransactionBodyMapper))
    contextual(AuthInfo::class, ProtobufSerializer(AuthInfoMapper))
    contextual(Signer::class, ProtobufSerializer(SignerMapper))
    contextual(SignMode::class, ProtobufSerializer(SignModeMapper))
    contextual(CompactBitArray::class, ProtobufSerializer(CompactBitArrayMapper))
    contextual(PublicKey::class, ProtobufAnySerializer(PublicKey::class))
    polymorphic(PublicKey::class) {
        mapper(PublicKeySecp256k1Mapper)
        mapper(PublicKeyEd25519Mapper)
    }
//    contextual(EnumMessage::class, PolymorphicKeyValueSerializer(EnumMessage::class))
//    contextual(Binary::class, ProtobufSerializer())
}