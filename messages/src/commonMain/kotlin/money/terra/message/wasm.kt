package money.terra.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import kotlinx.serialization.json.JsonObject
import money.terra.model.Coin
import money.terra.model.Message
import money.terra.type.Binary

@Serializable
data class StoreCodeMessage(
    val sender: String,
    @SerialName("wasm_byte_code") @Contextual val wasmByteCode: Binary,
) : Message()

@Serializable
data class InstantiateContractMessage(
    val sender: String,
    @SerialName("code_id") @Serializable(LongAsStringSerializer::class) val codeId: Long,
    val label: String,
    @SerialName("init_msg") val message: JsonObject,
    @SerialName("init_coins") val funds: List<Coin> = emptyList(),
    val admin: String? = null,
) : Message()

@Serializable
data class ExecuteContractMessage(
    val sender: String,
    val contract: String,
    @SerialName("execute_msg") @Contextual val message: JsonObject,
    @SerialName("coins") val funds: List<Coin> = emptyList(),
) : Message()

//@Serializable
//data class JsonExecuteContractMessage(
//    val sender: String,
//    val contract: String,
//    @SerialName("execute_msg") @Contextual val message: JsonObject,
//    @SerialName("coins") val funds: List<Coin> = emptyList(),
//) : Message()

@Serializable
data class MigrateContractMessage(
    val admin: String,
    val contract: String,
    @SerialName("new_code_id") @Serializable(LongAsStringSerializer::class) val newCodeId: Long,
    @SerialName("migrate_msg") val message: JsonObject,
) : Message()

@Serializable
data class UpdateContractAdminMessage(
    val admin: String,
    @SerialName("new_admin") val newAdmin: String,
    val contract: String,
) : Message()
