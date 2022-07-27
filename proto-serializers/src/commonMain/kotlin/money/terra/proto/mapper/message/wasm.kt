package money.terra.proto.mapper.message

import com.google.protobuf.ByteString
import cosmwasm.wasm.v1.*
import cosmwasm.wasm.v1.Tx.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import money.terra.message.*
import money.terra.proto.mapper.ProtobufTypeMapper
import money.terra.proto.mapper.type.fromProtobuf
import money.terra.proto.mapper.type.toProtobuf

object StoreCodeMessageMapper :
    ProtobufTypeMapper<StoreCodeMessage, MsgStoreCode> {

    override val descriptor = MsgStoreCode.getDescriptor()

    override val parser = MsgStoreCode.parser()

    override fun convert(obj: MsgStoreCode) = StoreCodeMessage(
        obj.sender,
        obj.wasmByteCode.fromProtobuf(),
    )

    override fun convert(obj: StoreCodeMessage) = msgStoreCode {
        sender = obj.sender
        wasmByteCode = obj.wasmByteCode.toProtobuf()
    }
}

object InstantiateContractMessageMapper :
    ProtobufTypeMapper<InstantiateContractMessage, MsgInstantiateContract> {

    override val descriptor = MsgInstantiateContract.getDescriptor()

    override val parser = MsgInstantiateContract.parser()

    override fun convert(obj: MsgInstantiateContract) = InstantiateContractMessage(
        obj.sender,
        obj.codeId,
        obj.label,
        Json.Default.decodeFromString(obj.msg.toStringUtf8()),
        obj.fundsList.fromProtobuf(),
        obj.admin,
    )

    override fun convert(obj: InstantiateContractMessage) = msgInstantiateContract {
        sender = obj.sender
        codeId = obj.codeId
        msg = ByteString.copyFrom(Json.Default.encodeToString(obj.message).toByteArray())
        funds.addAll(obj.funds.toProtobuf())
        label = obj.label
        obj.admin?.let { admin = it }
    }
}

object ExecuteContractMessageMapper :
    ProtobufTypeMapper<ExecuteContractMessage, MsgExecuteContract> {

    override val descriptor = MsgExecuteContract.getDescriptor()

    override val parser = MsgExecuteContract.parser()

    override fun convert(obj: MsgExecuteContract) = ExecuteContractMessage(
        obj.sender,
        obj.contract,
        Json.Default.decodeFromString(obj.msg.toStringUtf8()),
        obj.fundsList.fromProtobuf(),
    )

    override fun convert(obj: ExecuteContractMessage) = msgExecuteContract {
        sender = obj.sender
        contract = obj.contract
        msg = ByteString.copyFrom(Json.Default.encodeToString(obj.message).toByteArray())
        funds.addAll(obj.funds.toProtobuf())
    }
}

object MigrateContractMessageMapper :
    ProtobufTypeMapper<MigrateContractMessage, MsgMigrateContract> {

    override val descriptor = MsgMigrateContract.getDescriptor()

    override val parser = MsgMigrateContract.parser()

    override fun convert(obj: MsgMigrateContract) = MigrateContractMessage(
        obj.sender,
        obj.contract,
        obj.codeId,
        Json.Default.decodeFromString(obj.msg.toStringUtf8()),
    )

    override fun convert(obj: MigrateContractMessage) = msgMigrateContract {
        sender = obj.admin
        contract = obj.contract
        codeId = obj.newCodeId
        msg = ByteString.copyFrom(Json.Default.encodeToString(obj.message).toByteArray())
    }
}

object UpdateContractAdminMessageMapper :
    ProtobufTypeMapper<UpdateContractAdminMessage, MsgUpdateAdmin> {

    override val descriptor = MsgUpdateAdmin.getDescriptor()

    override val parser = MsgUpdateAdmin.parser()

    override fun convert(obj: MsgUpdateAdmin) = UpdateContractAdminMessage(
        obj.sender,
        obj.newAdmin,
        obj.contract,
    )

    override fun convert(obj: UpdateContractAdminMessage) = msgUpdateAdmin {
        sender = obj.admin
        newAdmin = obj.newAdmin
        contract = obj.contract
    }
}
