package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.type.Binary

@Serializable
data class Signature(
    @Contextual val signature: Binary,
    @SerialName("pub_key") val publicKey: PublicKey,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long? = null,
    @Serializable(LongAsStringSerializer::class) val sequence: Long? = null,
) {

    constructor(
        signature: Binary,
        publicKey: Binary,
        accountNumber: Long? = null,
        sequence: Long? = null,
        publicKeyType: String = "tendermint/PubKeySecp256k1",
    ) : this(
        signature,
        PublicKey(publicKey, publicKeyType),
        accountNumber,
        sequence,
    )
}