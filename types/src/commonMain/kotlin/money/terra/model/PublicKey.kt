package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import money.terra.type.Binary

@Serializable
data class PublicKey(
    @Contextual val value: Binary,
    val type: String = "tendermint/PubKeySecp256k1",
)