package money.terra.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import money.terra.model.Coin
import money.terra.model.EnumMessage
import money.terra.model.Message
import money.terra.type.Binary
import money.terra.type.ULongAsStringSerializer

@Serializable
data class StoreCodeMessage(
    val sender: String,
    @SerialName("wasm_byte_code") @Contextual val wasmByteCode: Binary,
) : Message()

@Serializable
data class MigrateCodeMessage(
    @SerialName("code_id") @Serializable(ULongAsStringSerializer::class) val codeId: ULong,
    val sender: String,
    @SerialName("wasm_byte_code") @Contextual val wasmByteCode: Binary,
) : Message()

@Serializable
data class InstantiateContractMessage(
    val sender: String,
    @SerialName("code_id") @Serializable(ULongAsStringSerializer::class) val codeId: ULong,
    @SerialName("init_msg") val message: JsonObject,
    @SerialName("init_coins") val funds: List<Coin> = emptyList(),
    val admin: String? = null,
) : Message()

@Serializable
data class ExecuteContractMessage(
    val sender: String,
    val contract: String,
    @SerialName("execute_msg") @Contextual val message: EnumMessage,
    @SerialName("coins") val funds: List<Coin> = emptyList(),
) : Message()

@Serializable
data class MigrateContractMessage(
    val admin: String,
    val contract: String,
    @SerialName("new_code_id") @Serializable(ULongAsStringSerializer::class) val newCodeId: ULong,
    @SerialName("migrate_msg") val message: JsonObject,
) : Message()

@Serializable
data class UpdateContractAdminMessage(
    val admin: String,
    val newAdmin: String,
    val contract: String,
) : Message()

@Serializable
data class ClearContractAdminMessage(
    val admin: String,
    val contract: String,
) : Message()
