package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.type.Binary

@Serializable
data class TransactionResponse(
    val height: Long,
    @SerialName("txhash") val transactionHash: String,
    @SerialName("codespace") val codeSpace: String,
    val code: Int,
    @Contextual val data: Binary, //TODO: base64 decode
    @SerialName("raw_log") val rawLog: String,
    val logs: List<TransactionLog>,
    val info: String,
    val gasWanted: Long,
    val gasUsed: Long,
    val transaction: Transaction,
    val timestamp: String,
    val events: List<TransactionBinaryEvent>,
)

@Serializable
@Deprecated("Legacy format")
data class TransactionResult(
    @Serializable(LongAsStringSerializer::class) val height: Long,
    @SerialName("txhash") val transactionHash: String,
    @SerialName("raw_log") val rawLog: String,
    @SerialName("gas_wanted") @Serializable(LongAsStringSerializer::class) val gasWanted: Long,
    @SerialName("gas_used") @Serializable(LongAsStringSerializer::class) val gasUsed: Long,
    @SerialName("tx") val transaction: TypeWrapper<StdTx>,
    val timestamp: String,
    @SerialName("codespace") val codeSpace: String? = null,
    val code: Int? = null,
    @Contextual val data: Binary? = null,
    val logs: List<TransactionLog>? = null,
    val events: List<TransactionBinaryEvent>? = null,
) {

    val isSuccess = code == null || code == 0
}
