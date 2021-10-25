package money.terra.proposal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import money.terra.message.Proposal

val TreasuryProposalSerializerModule = SerializersModule {
    polymorphic(Proposal::class) {
        subclass(TaxRateUpdateProposal::class)
        subclass(RewardWeightUpdateProposal::class)
    }
}

@Serializable
@SerialName("treasury/TaxRateUpdateProposal")
data class TaxRateUpdateProposal(
    val title: String,
    val description: String,
    @SerialName("tax_rate") val taxRate: String,
) : Proposal()

@Serializable
@SerialName("treasury/RewardWeightUpdateProposal")
data class RewardWeightUpdateProposal(
    val title: String,
    val description: String,
    @SerialName("reward_weight") val rewardWeight: String,
) : Proposal()