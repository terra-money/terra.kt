package money.terra.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Coin
import money.terra.model.Message

@Serializable
data class SetWithdrawAddressMessage(
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("withdraw_address") val withdrawAddress: String,
) : Message()

@Serializable
data class WithdrawDelegatorRewardMessage(
    @SerialName("delegator_address") val delegatorAddress: String,
    @SerialName("validator_address") val validatorAddress: String,
) : Message()

@Serializable
data class WithdrawValidatorCommissionMessage(
    @SerialName("validator_address") val validatorAddress: String,
) : Message()

@Serializable
data class FundCommunityPoolMessage(
    val amount: List<Coin>,
    val depositor: String,
) : Message()