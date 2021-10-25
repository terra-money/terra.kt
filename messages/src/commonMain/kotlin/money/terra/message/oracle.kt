package money.terra.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Message

@Serializable
data class ExchangeRatePrevoteMessage(
    val hash: String,
    val denom: String,
    val feeder: String,
    val validator: String,
) : Message()

@Serializable
data class ExchangeRateVoteMessage(
    @SerialName("exchange_rate") val exchangeRate: String,
    val salt: String,
    val denom: String,
    val feeder: String,
    val validator: String,
) : Message()

@Serializable
data class DelegateFeedConsentMessage(
    val operator: String,
    val delegate: String,
) : Message()

@Serializable
data class AggregateExchangeRatePrevoteMessage(
    val hash: String,
    val feeder: String,
    val validator: String,
) : Message()

@Serializable
data class AggregateExchangeRateVote(
    val salt: String,
    @SerialName("exchange_rates") val exchangeRates: String,
    val feeder: String,
    val validator: String,
) : Message()

