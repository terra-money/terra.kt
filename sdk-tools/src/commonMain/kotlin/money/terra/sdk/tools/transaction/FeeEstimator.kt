package money.terra.sdk.tools.transaction

import money.terra.model.*
import money.terra.type.Decimal
import money.terra.type.toDecimal
import money.terra.type.toUint128

class EstimateFeeException(
    val transactionBody: TransactionBody,
    val gasPrice: CoinDecimal,
    val gasAdjustment: Float,
    val reason: String,
    cause: Throwable? = null,
) : Exception(reason, cause)

abstract class FeeEstimator {

    val gasPricesProvider: GasPricesProvider

    var defaultGasAdjustment: Float = 2f
    var defaultFeeDenomination: String = "uluna"

    constructor(gasPrices: Map<String, Decimal>) {
        gasPricesProvider = StaticGasPricesProvider(gasPrices)
    }

    constructor(gasPricesProvider: GasPricesProvider) {
        this.gasPricesProvider = gasPricesProvider
    }

    @Throws(EstimateFeeException::class)
    suspend fun estimate(
        transactionBody: TransactionBody,
        signers: List<Signer>,
        feeDenomination: String = defaultFeeDenomination,
        gasAdjustment: Float = defaultGasAdjustment,
    ): Fee {
        val gasPrice = gasPricesProvider.get(feeDenomination)

        return estimate(transactionBody, signers, gasPrice, gasAdjustment)
    }

    suspend fun estimate(gasAmount: Long, feeDenomination: String = defaultFeeDenomination): Fee {
        val gasPrice = gasPricesProvider.get(feeDenomination)
        val feeAmount = (gasAmount.toDecimal() * gasPrice.amount).toUint128()

        return Fee(gasAmount, listOf(Coin(feeDenomination, feeAmount)))
    }

    @Throws(EstimateFeeException::class)
    protected abstract suspend fun estimate(
        transactionBody: TransactionBody,
        signers: List<Signer>,
        gasPrice: CoinDecimal,
        gasAdjustment: Float,
    ): Fee
}
