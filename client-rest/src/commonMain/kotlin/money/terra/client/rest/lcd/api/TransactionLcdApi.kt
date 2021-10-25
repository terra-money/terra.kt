package money.terra.client.rest.lcd.api

import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import money.terra.client.rest.HttpClient
import money.terra.client.rest.api.*
import money.terra.client.rest.model.Result
import money.terra.model.CoinDecimal
import money.terra.model.Message
import money.terra.model.Transaction
import money.terra.model.TransactionResult

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class TransactionLcdApi(
    private val client: HttpClient,
    private val chainId: String,
) : TransactionApi {

    override fun getByHash(
        transactionHash: String,
    ): Deferred<TransactionResult?> = CoroutineScope(Dispatchers.Unconfined).async {
        try {
            client.get<TransactionResult>("/txs/$transactionHash").await()
        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                return@async null
            }

            throw e
        }
    }

    override fun broadcastAsync(transaction: Transaction): Deferred<BroadcastAsyncResult> {
        return client.post("/txs", BroadcastAsyncRequest(transaction))
    }

    override fun broadcastSync(transaction: Transaction): Deferred<BroadcastSyncResult> {
        return client.post("/txs", BroadcastSyncRequest(transaction))
    }

    override fun broadcastBlock(transaction: Transaction): Deferred<BroadcastBlockResult> {
        return client.post("/txs", BroadcastBlockRequest(transaction))
    }

    override fun estimateFee(
        messages: List<Message>,
        senderAddress: String,
        senderAccountNumber: ULong,
        senderSequence: ULong,
        gasPrices: List<CoinDecimal>,
        gasAdjustment: Float,
    ): Deferred<Result<EstimateFeeResult>> = client.post(
        "/txs/estimate_fee",
        EstimateFeeRequest(
            messages,
            chainId,
            senderAddress,
            senderAccountNumber,
            senderSequence,
            gasPrices,
            gasAdjustment,
        ),
    )
}