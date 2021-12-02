package money.terra.proposal

import kotlinx.serialization.Serializable
import money.terra.message.Proposal
import money.terra.model.Coin

@Serializable
data class CommunityPoolSpendProposal(
    val title: String,
    val description: String,
    val recipient: String,
    val amount: List<Coin>,
) : Proposal()