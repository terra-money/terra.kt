package money.terra.sdk.proto.wallet

import com.google.protobuf.kotlin.toByteString
import cosmos.tx.v1beta1.TxOuterClass.SignDoc
import cosmos.tx.v1beta1.signDoc
import kotlinx.serialization.modules.SerializersModule
import money.terra.key.Key
import money.terra.model.SignMode
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.serializer.ProtobufSerializer
import money.terra.sdk.tools.transaction.TransactionSignDocument
import money.terra.sdk.tools.transaction.TransactionSigner

object ProtoTransactionSigner : TransactionSigner {

    init {
        ProtobufFormat.registerSerializersModule(SerializersModule {
            contextual(TransactionSignDocument::class, ProtobufSerializer(TransactionSignDocumentMapper))
        })
    }

    override val signMode = SignMode.Direct

    override suspend fun sign(
        key: Key,
        data: TransactionSignDocument,
    ): ByteArray = key.sign(serializeSignData(data)).await()

    fun serializeSignData(data: TransactionSignDocument): ByteArray {
        return ProtobufFormat.encodeToByteArray(data)
    }
}

object TransactionSignDocumentMapper : ProtobufTypeMapper<TransactionSignDocument, SignDoc> {

    override val descriptor = SignDoc.getDescriptor()

    override val parser = SignDoc.parser()

    override fun convert(obj: SignDoc) = TransactionSignDocument(
        ProtobufFormat.decodeFromByteArray(obj.bodyBytes.toByteArray()),
        ProtobufFormat.decodeFromByteArray(obj.authInfoBytes.toByteArray()),
        obj.chainId,
        obj.accountNumber,
    )

    override fun convert(obj: TransactionSignDocument) = signDoc {
        bodyBytes = ProtobufFormat.encodeToByteArray(obj.body).toByteString()
        authInfoBytes = ProtobufFormat.encodeToByteArray(obj.authInfo).toByteString()
        chainId = obj.chainId
        accountNumber = obj.accountNumber
    }
}