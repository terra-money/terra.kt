package money.terra.client.rest.provider

import money.terra.client.rest.RestClientResponseException
import money.terra.client.rest.api.TransactionApi
import money.terra.model.CoinDecimal
import money.terra.model.Fee
import money.terra.model.Message
import money.terra.sdk.tools.transaction.EstimateFeeException
import money.terra.sdk.tools.transaction.FeeEstimator
import money.terra.sdk.tools.transaction.GasPricesProvider

class NodeFeeEstimator(
    private val transactionApi: TransactionApi,
    gasPricesProvider: GasPricesProvider,
) : FeeEstimator(gasPricesProvider) {

    override suspend fun estimate(
        messages: List<Message>,
        senderAddress: String,
        senderAccountNumber: ULong,
        senderSequence: ULong,
        gasPrices: List<CoinDecimal>,
        gasAdjustment: Float,
    ): Fee = try {
        transactionApi.estimateFee(
            messages,
            senderAddress,
            senderAccountNumber,
            senderSequence,
            gasPrices,
            gasAdjustment,
        ).await().result.fee
    } catch (e: Exception) {
        val reason = if (e is RestClientResponseException) {
            e.responseBody
        } else {
            e.message ?: "Occur error in ${this::class.qualifiedName ?: "NodeFeeEstimator"}"
        }

        throw EstimateFeeException(
            messages,
            senderAddress,
            senderAccountNumber,
            senderSequence,
            gasPrices,
            gasAdjustment,
            reason,
            e,
        )
    }
}