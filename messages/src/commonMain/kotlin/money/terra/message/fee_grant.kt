package money.terra.message

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message

@Serializable
data class GrantAllowanceMessage(
    val granter: String,
    val grantee: String,
    @Contextual val allowance: Allowance,
) : Message()

@Serializable
data class RevokeAllowanceMessage(
    val granter: String,
    val grantee: String,
) : Message()

abstract class Allowance

@Serializable
data class BasicAllowance(
    @SerialName("spend_limit") val spendLimit: List<Coin>,
    val expiration: LocalDateTime,
) : Allowance()

@Serializable
data class PeriodicAllowance(
    val basic: BasicAllowance,
    val period: DateTimePeriod,
    @SerialName("period_spend_limit") val periodSpendLimit: List<Coin>,
    @SerialName("period_can_spend") val periodCanSpend: List<Coin>,
    @SerialName("period_reset") val periodReset: LocalDateTime,
) : Allowance()

@Serializable
data class AllowedMsgAllowance(
    @Contextual val allowance: Allowance,
    @SerialName("allowed_messages") val allowedMessages: List<String>,
) : Allowance()
