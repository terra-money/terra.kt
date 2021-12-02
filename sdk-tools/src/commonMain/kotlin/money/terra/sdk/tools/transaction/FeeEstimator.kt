package money.terra.sdk.tools.transaction

import money.terra.model.Coin
import money.terra.model.CoinDecimal
import money.terra.model.Fee
import money.terra.model.Message
import money.terra.type.Decimal
import money.terra.type.toDecimal
import money.terra.type.toUint128

class EstimateFeeException(
    val messages: List<Message>,
    val requesterAddress: String,
    val requesterAccountNumber: Long,
    val requesterSequence: Long,
    val gasPrices: List<CoinDecimal>,
    val gasAdjustment: Float,
    val reason: String,
    cause: Throwable? = null,
) : Exception(reason, cause)

abstract class FeeEstimator {

    val gasPricesProvider: GasPricesProvider

    var defaultGasAdjustment: Float = 1.4f
    var defaultFeeDenomination: String = "uluna"

    constructor(gasPrices: Map<String, Decimal>) {
        gasPricesProvider = StaticGasPricesProvider(gasPrices)
    }

    constructor(gasPricesProvider: GasPricesProvider) {
        this.gasPricesProvider = gasPricesProvider
    }

    @Throws(EstimateFeeException::class)
    suspend fun estimate(
        messages: List<Message>,
        senderAddress: String,
        senderAccountNumber: Long,
        senderSequence: Long,
        feeDenomination: String = defaultFeeDenomination,
        gasAdjustment: Float = defaultGasAdjustment,
    ): Fee {
        val gasPrice = gasPricesProvider.get(feeDenomination)

        return estimate(
            messages,
            senderAddress,
            senderAccountNumber,
            senderSequence,
            listOf(CoinDecimal(feeDenomination, gasPrice)),
            gasAdjustment,
        )
    }

    suspend fun estimate(gasAmount: Long, feeDenomination: String = defaultFeeDenomination): Fee {
        val gasPrice = gasPricesProvider.get(feeDenomination)
        val feeAmount = (gasAmount.toDecimal() * gasPrice).toUint128()

        return Fee(gasAmount, listOf(Coin(feeDenomination, feeAmount)))
    }

    @Throws(EstimateFeeException::class)
    protected abstract suspend fun estimate(
        messages: List<Message>,
        senderAddress: String,
        senderAccountNumber: Long,
        senderSequence: Long,
        gasPrices: List<CoinDecimal>,
        gasAdjustment: Float,
    ): Fee
}
