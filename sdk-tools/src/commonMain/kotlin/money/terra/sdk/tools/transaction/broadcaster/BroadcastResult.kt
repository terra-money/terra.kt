package money.terra.sdk.tools.transaction.broadcaster

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.model.TransactionLog

interface BroadcastResult {
    val transactionHash: String
    val codeSpace: String?
    val code: Int?
    val rawLog: String?
    val logs: List<TransactionLog>?
}

val BroadcastResult.isSuccess: Boolean
    get() = code == null || code == 0

@Serializable
data class BroadcastAsyncResult(
    @SerialName("txhash") override val transactionHash: String,
    @SerialName("codespace") override val codeSpace: String? = null,
    override val code: Int? = null,
    @SerialName("raw_log") override val rawLog: String? = null,
    override val logs: List<TransactionLog>? = null,
) : BroadcastResult

@Serializable
data class BroadcastSyncResult(
    @SerialName("txhash") override val transactionHash: String,
    @SerialName("codespace") override val codeSpace: String? = null,
    override val code: Int? = null,
    @SerialName("raw_log") override val rawLog: String? = null,
    override val logs: List<TransactionLog>? = null,
) : BroadcastResult

@Serializable
data class BroadcastBlockResult(
    @Serializable(LongAsStringSerializer::class) val height: Long,
    @SerialName("txhash") override val transactionHash: String,
    @SerialName("codespace") override val codeSpace: String? = null,
    override val code: Int? = null,
    @SerialName("raw_log") override val rawLog: String? = null,
    override val logs: List<TransactionLog>? = null,
    val gasUsed: Long? = null,
    val gasWanted: Long? = null,
) : BroadcastResult