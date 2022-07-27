package money.terra.client.grpc.provider

import money.terra.client.grpc.api.TransactionApi
import money.terra.model.*
import money.terra.sdk.tools.transaction.EstimateFeeException
import money.terra.sdk.tools.transaction.FeeEstimator
import money.terra.sdk.tools.transaction.GasPricesProvider
import money.terra.type.Binary
import money.terra.type.Decimal
import money.terra.type.Uint128
import money.terra.type.toUint128
import kotlin.math.ceil

class NodeFeeEstimator(
    private val transactionApi: TransactionApi,
    gasPricesProvider: GasPricesProvider,
) : FeeEstimator(gasPricesProvider) {

    override suspend fun estimate(
        transactionBody: TransactionBody,
        signers: List<Signer>,
        gasPrice: CoinDecimal,
        gasAdjustment: Float,
    ): Fee = try {
        val signatures = mutableListOf<Binary>()
        repeat(signers.size) {
            signatures.add(Binary(byteArrayOf()))
        }
        val result = transactionApi.simulate(Transaction(transactionBody, AuthInfo(signers, Fee.EMPTY), signatures)).await()
        val gasUsed = result.gasInfo.gasUsed
        val gas = (gasUsed * gasAdjustment).toLong()
        val fee = Coin(gasPrice.denomination, Uint128(ceil((gasPrice.amount.toDouble() * gas)).toLong().toString()))

        Fee(gas, listOf(fee))
    } catch (e: Exception) {
        val reason = e.message ?: "Occur error in ${this::class.qualifiedName ?: "NodeFeeEstimator"}"

        throw EstimateFeeException(
            transactionBody,
            gasPrice,
            gasAdjustment,
            reason,
            e,
        )
    }
}