package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.type.Binary
import money.terra.type.ULongAsStringSerializer

@Serializable
data class TransactionResult(
    @Serializable(ULongAsStringSerializer::class) val height: ULong,
    @SerialName("txhash") val transactionHash: String,
    @SerialName("raw_log") val rawLog: String,
    @SerialName("gas_wanted") @Serializable(ULongAsStringSerializer::class) val gasWanted: ULong,
    @SerialName("gas_used") @Serializable(ULongAsStringSerializer::class) val gasUsed: ULong,
    @SerialName("tx") val transaction: TypeWrapper<Transaction>,
    val timestamp: String,
    @SerialName("codespace") val codeSpace: String? = null,
    val code: Int? = null,
    @Contextual val data: Binary? = null,
    val logs: List<TransactionLog>? = null,
) {

    val isSuccess = code == null || code == 0
}
