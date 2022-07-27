package money.terra.proto.mapper.type

import com.google.protobuf.kotlin.toByteString
import cosmos.base.abci.v1beta1.*
import cosmos.base.abci.v1beta1.Abci.ABCIMessageLog
import cosmos.base.abci.v1beta1.Abci.TxResponse
import cosmos.base.v1beta1.CoinOuterClass
import cosmos.base.v1beta1.coin
import cosmos.crypto.multisig.v1beta1.compactBitArray
import cosmos.tx.signing.v1beta1.Signing
import cosmos.tx.v1beta1.*
import cosmos.tx.v1beta1.ModeInfoKt.multi
import cosmos.tx.v1beta1.ModeInfoKt.single
import kr.jadekim.common.encoder.decodeBase64
import kr.jadekim.common.encoder.encodeBase64
import money.terra.model.*
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.AuthInfoMapper.fromProtobuf
import money.terra.proto.mapper.type.AuthInfoMapper.toProtobuf
import money.terra.proto.mapper.type.CompactBitArrayMapper.fromProtobuf
import money.terra.proto.mapper.type.CompactBitArrayMapper.toProtobuf
import money.terra.proto.mapper.type.FeeMapper.fromProtobuf
import money.terra.proto.mapper.type.FeeMapper.toProtobuf
import money.terra.proto.mapper.type.SignModeMapper.fromProtobuf
import money.terra.proto.mapper.type.SignModeMapper.toProtobuf
import money.terra.proto.mapper.type.SignerMapper.fromProtobuf
import money.terra.proto.mapper.type.SignerMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionBinaryEventAttributeMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionBinaryEventAttributeMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionBinaryEventMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionBinaryEventMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionBodyMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionBodyMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionEventAttributeMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionEventAttributeMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionLogMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionLogMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionStringEventMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionStringEventMapper.toProtobuf
import money.terra.type.Binary
import money.terra.type.CompactBitArray
import money.terra.type.Uint128
import tendermint.abci.Types
import tendermint.abci.event
import tendermint.abci.eventAttribute

object CoinMapper : ProtobufTypeMapper<Coin, CoinOuterClass.Coin> {

    override val descriptor = CoinOuterClass.Coin.getDescriptor()

    override val parser = CoinOuterClass.Coin.parser()

    override fun convert(obj: CoinOuterClass.Coin) = Coin(
        obj.denom,
        Uint128(obj.amount),
    )

    override fun convert(obj: Coin) = coin {
        denom = obj.denomination
        amount = obj.amount.toString()
    }
}

fun List<Coin>.toProtobuf(): List<CoinOuterClass.Coin> = map(CoinMapper::convert)

fun List<CoinOuterClass.Coin>.fromProtobuf() = map(CoinMapper::convert)

object FeeMapper : ProtobufTypeMapper<Fee, TxOuterClass.Fee> {

    override val descriptor = TxOuterClass.Fee.getDescriptor()

    override val parser = TxOuterClass.Fee.parser()

    override fun convert(obj: TxOuterClass.Fee) = Fee(
        obj.gasLimit,
        obj.amountList.fromProtobuf(),
        obj.payer,
        obj.granter,
    )

    override fun convert(obj: Fee) = fee {
        obj.granter?.let { granter = it }
        obj.payer?.let { payer = it }
        gasLimit = obj.gasLimit
        amount.addAll(obj.feeAmount.toProtobuf())
    }
}

object TransactionResponseMapper : ProtobufTypeMapper<TransactionResponse, TxResponse> {

    override val descriptor = TxResponse.getDescriptor()

    override val parser = TxResponse.parser()

    override fun convert(obj: TxResponse) = TransactionResponse(
        obj.height,
        obj.txhash,
        obj.codespace,
        obj.code,
        Binary(obj.data.decodeBase64()),
        obj.rawLog,
        obj.logsList.map { it.fromProtobuf() },
        obj.info,
        obj.gasWanted,
        obj.gasUsed,
        ProtobufFormat.decodeFromAny(obj.tx),
        obj.timestamp,
        obj.eventsList.map { it.fromProtobuf() },
    )

    override fun convert(obj: TransactionResponse) = txResponse {
        height = obj.height
        txhash = obj.transactionHash
        codespace = obj.codeSpace
        code = obj.code
        data = obj.data.data.encodeBase64()
        rawLog = obj.rawLog
        logs.addAll(obj.logs.map { it.toProtobuf() })
        info = obj.info
        gasWanted = obj.gasWanted
        gasUsed = obj.gasUsed
        tx = ProtobufFormat.encodeToAny(obj.transaction)
        timestamp = obj.timestamp
        events.addAll(obj.events.map { it.toProtobuf() })
    }
}

object TransactionLogMapper : ProtobufTypeMapper<TransactionLog, ABCIMessageLog> {

    override val descriptor = ABCIMessageLog.getDescriptor()

    override val parser = ABCIMessageLog.parser()

    override fun convert(obj: ABCIMessageLog) = TransactionLog(
        obj.msgIndex,
        obj.log,
        obj.eventsList.map { it.fromProtobuf() }
    )

    override fun convert(obj: TransactionLog) = aBCIMessageLog {
        msgIndex = obj.index
        log = obj.log
        events.addAll(obj.events.map { it.toProtobuf() })
    }
}

object TransactionStringEventMapper : ProtobufTypeMapper<TransactionStringEvent, Abci.StringEvent> {

    override val descriptor = Abci.StringEvent.getDescriptor()

    override val parser = Abci.StringEvent.parser()

    override fun convert(obj: Abci.StringEvent) = TransactionStringEvent(
        obj.type,
        obj.attributesList.map { it.fromProtobuf() }
    )

    override fun convert(obj: TransactionStringEvent) = stringEvent {
        type = obj.type
        attributes.addAll(obj.attributes.map { it.toProtobuf() })
    }
}

object TransactionEventAttributeMapper : ProtobufTypeMapper<StringAttribute, Abci.Attribute> {

    override val descriptor = Abci.Attribute.getDescriptor()

    override val parser = Abci.Attribute.parser()

    override fun convert(obj: Abci.Attribute) = StringAttribute(
        obj.key,
        obj.value,
    )

    override fun convert(obj: StringAttribute) = attribute {
        key = obj.key
        value = obj.value
    }
}

object TransactionBinaryEventMapper : ProtobufTypeMapper<TransactionBinaryEvent, Types.Event> {

    override val descriptor = Types.Event.getDescriptor()

    override val parser = Types.Event.parser()

    override fun convert(obj: Types.Event) = TransactionBinaryEvent(
        obj.type,
        obj.attributesList.map { it.fromProtobuf() }
    )

    override fun convert(obj: TransactionBinaryEvent) = event {
        type = obj.type
        attributes.addAll(obj.attributes.map { it.toProtobuf() })
    }
}

object TransactionBinaryEventAttributeMapper : ProtobufTypeMapper<BinaryAttribute, Types.EventAttribute> {

    override val descriptor = Types.EventAttribute.getDescriptor()

    override val parser = Types.EventAttribute.parser()

    override fun convert(obj: Types.EventAttribute) = BinaryAttribute(
        Binary(obj.key.toByteArray()),
        Binary(obj.value.toByteArray()),
    )

    override fun convert(obj: BinaryAttribute) = eventAttribute {
        key = obj.key.data.toByteString()
        value = obj.value.data.toByteString()
    }
}

object TransactionMapper : ProtobufTypeMapper<Transaction, TxOuterClass.Tx> {

    override val descriptor = TxOuterClass.Tx.getDescriptor()

    override val parser = TxOuterClass.Tx.parser()

    override fun convert(obj: TxOuterClass.Tx) = Transaction(
        obj.body.fromProtobuf(),
        obj.authInfo.fromProtobuf(),
        obj.signaturesList.map { it.fromProtobuf() },
    )

    override fun convert(obj: Transaction) = tx {
        body = obj.body.toProtobuf()
        authInfo = obj.authInfo.toProtobuf()
        signatures.addAll(obj.signatures.map { it.toProtobuf() })
    }
}

object TransactionBodyMapper : ProtobufTypeMapper<TransactionBody, TxOuterClass.TxBody> {

    override val descriptor = TxOuterClass.TxBody.getDescriptor()

    override val parser = TxOuterClass.TxBody.parser()

    override fun convert(obj: TxOuterClass.TxBody) = TransactionBody(
        obj.messagesList.map { ProtobufFormat.decodeFromAny(it) },
        obj.memo,
        obj.timeoutHeight,
        obj.extensionOptionsList.map { ProtobufFormat.decodeFromAny(it) },
        obj.nonCriticalExtensionOptionsList.map { ProtobufFormat.decodeFromAny(it) },
    )

    override fun convert(obj: TransactionBody) = txBody {
        messages.addAll(obj.messages.map { ProtobufFormat.encodeToAny(it) })
        memo = obj.memo
        timeoutHeight = obj.timeoutHeight
        extensionOptions.addAll(obj.extensionOptions.map { ProtobufFormat.encodeToAny(it) })
        nonCriticalExtensionOptions.addAll(obj.nonCriticalExtensionOptions.map { ProtobufFormat.encodeToAny(it) })
    }
}

object AuthInfoMapper : ProtobufTypeMapper<AuthInfo, TxOuterClass.AuthInfo> {

    override val descriptor = TxOuterClass.AuthInfo.getDescriptor()

    override val parser = TxOuterClass.AuthInfo.parser()

    override fun convert(obj: TxOuterClass.AuthInfo) = AuthInfo(
        obj.signerInfosList.map { it.fromProtobuf() },
        obj.fee.fromProtobuf(),
    )

    override fun convert(obj: AuthInfo) = authInfo {
        signerInfos.addAll(obj.signerInfos.map { it.toProtobuf() })
        fee = obj.fee.toProtobuf()
    }
}

object SignerMapper : ProtobufTypeMapper<Signer, TxOuterClass.SignerInfo> {

    override val descriptor = TxOuterClass.SignerInfo.getDescriptor()

    override val parser = TxOuterClass.SignerInfo.parser()

    override fun convert(obj: TxOuterClass.SignerInfo) = Signer(
        ProtobufFormat.decodeFromAny(obj.publicKey),
        obj.modeInfo.fromProtobuf(),
        obj.sequence,
    )

    override fun convert(obj: Signer) = signerInfo {
        publicKey = ProtobufFormat.encodeToAny(obj.publicKey)
        modeInfo = obj.modeInfo.toProtobuf()
        sequence = obj.sequence
    }
}

object SignModeMapper : ProtobufTypeMapper<SignMode, TxOuterClass.ModeInfo> {

    override val descriptor = TxOuterClass.ModeInfo.getDescriptor()

    override val parser = TxOuterClass.ModeInfo.parser()

    override fun convert(obj: TxOuterClass.ModeInfo): SignMode = if (obj.hasSingle()) {
        when (obj.single.mode!!) {
            Signing.SignMode.SIGN_MODE_DIRECT -> SignMode.Direct
            Signing.SignMode.SIGN_MODE_UNSPECIFIED -> SignMode.Unspecified
            Signing.SignMode.SIGN_MODE_TEXTUAL -> SignMode.Textual
            Signing.SignMode.SIGN_MODE_LEGACY_AMINO_JSON -> SignMode.LegacyAminoJson
            Signing.SignMode.SIGN_MODE_EIP_191 -> SignMode.Eip191
            Signing.SignMode.UNRECOGNIZED -> throw IllegalArgumentException("Unrecognized SignMode")
        }
    } else {
        val multi = obj.multi
        SignMode.Multiple(multi.bitarray.fromProtobuf(), multi.modeInfosList.map(this::convert))
    }

    override fun convert(obj: SignMode) = modeInfo {
        when (obj) {
            SignMode.Direct -> single = single {
                mode = Signing.SignMode.SIGN_MODE_DIRECT
            }
            SignMode.LegacyAminoJson -> single = single {
                mode = Signing.SignMode.SIGN_MODE_LEGACY_AMINO_JSON
            }
            SignMode.Textual -> single = single {
                mode = Signing.SignMode.SIGN_MODE_TEXTUAL
            }
            SignMode.Eip191 -> single = single {
                mode = Signing.SignMode.SIGN_MODE_EIP_191
            }
            SignMode.Unspecified -> single = single {
                mode = Signing.SignMode.SIGN_MODE_UNSPECIFIED
            }
            is SignMode.Multiple -> multi = multi {
                bitarray = obj.bitArray.toProtobuf()
                modeInfos.addAll(obj.modeInfos.map { it.toProtobuf() })
            }
        }
    }
}

object CompactBitArrayMapper :
    ProtobufTypeMapper<CompactBitArray, cosmos.crypto.multisig.v1beta1.Multisig.CompactBitArray> {

    override val descriptor = cosmos.crypto.multisig.v1beta1.Multisig.CompactBitArray.getDescriptor()

    override val parser = cosmos.crypto.multisig.v1beta1.Multisig.CompactBitArray.parser()

    override fun convert(obj: cosmos.crypto.multisig.v1beta1.Multisig.CompactBitArray) = CompactBitArray(
        obj.extraBitsStored,
        obj.elems.fromProtobuf(),
    )

    override fun convert(obj: CompactBitArray) = compactBitArray {
        extraBitsStored = obj.extraBitsStored
        elems = obj.elements.toProtobuf()
    }
}

object PublicKeySecp256k1Mapper :
    ProtobufTypeMapper<PublicKey.Secp256k1, cosmos.crypto.secp256k1.Keys.PubKey> {

    override val descriptor = cosmos.crypto.secp256k1.Keys.PubKey.getDescriptor()

    override val parser = cosmos.crypto.secp256k1.Keys.PubKey.parser()

    override fun convert(obj: cosmos.crypto.secp256k1.Keys.PubKey) = PublicKey.Secp256k1(
        obj.key.fromProtobuf()
    )

    override fun convert(obj: PublicKey.Secp256k1) = cosmos.crypto.secp256k1.pubKey {
        key = obj.value.toProtobuf()
    }
}

object PublicKeyEd25519Mapper :
    ProtobufTypeMapper<PublicKey.Ed25519, cosmos.crypto.ed25519.Keys.PubKey> {

    override val descriptor = cosmos.crypto.ed25519.Keys.PubKey.getDescriptor()

    override val parser = cosmos.crypto.ed25519.Keys.PubKey.parser()

    override fun convert(obj: cosmos.crypto.ed25519.Keys.PubKey) = PublicKey.Ed25519(
        obj.key.fromProtobuf()
    )

    override fun convert(obj: PublicKey.Ed25519) = cosmos.crypto.ed25519.pubKey {
        key = obj.value.toProtobuf()
    }
}
