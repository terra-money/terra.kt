package money.terra.proposal

import kotlinx.serialization.Serializable
import money.terra.message.Proposal

@Serializable
data class TextProposal(
    val title: String,
    val description: String,
) : Proposal()