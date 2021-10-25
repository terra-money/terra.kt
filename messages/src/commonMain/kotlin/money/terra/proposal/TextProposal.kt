package money.terra.proposal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import money.terra.message.Proposal

val TextProposalSerializerModule = SerializersModule {
    polymorphic(Proposal::class) {
        subclass(TextProposal::class)
    }
}

@Serializable
@SerialName("gov/TextProposal")
data class TextProposal(
    val title: String,
    val description: String,
) : Proposal()