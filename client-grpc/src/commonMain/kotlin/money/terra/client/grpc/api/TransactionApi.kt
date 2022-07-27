package money.terra.client.grpc.api

import kotlinx.coroutines.Deferred
import kotlinx.serialization.SerialName
import money.terra.model.Transaction
import money.terra.model.TransactionResponse

interface TransactionApi {

    fun simulate(transaction: Transaction): Deferred<SimulateResult>

    fun getByHash(transactionHash: String): Deferred<GetTransactionResponse?>

    fun broadcastAsync(transaction: Transaction): Deferred<TransactionResponse>

    fun broadcastSync(transaction: Transaction): Deferred<TransactionResponse>

    fun broadcastBlock(transaction: Transaction): Deferred<TransactionResponse>
}

@kotlinx.serialization.Serializable
data class SimulateResult(
    val gasInfo: GasInfo,
)

@kotlinx.serialization.Serializable
data class GasInfo(
    val gasUsed: Long,
    val gasWanted: Long,
)

@kotlinx.serialization.Serializable
data class GetTransactionResponse(
    @SerialName("tx") val transaction: Transaction,
    @SerialName("tx_response") val transactionResponse: TransactionResponse,
)
