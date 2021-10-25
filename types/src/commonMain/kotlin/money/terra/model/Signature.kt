package money.terra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.type.Binary
import money.terra.type.ULongAsStringSerializer

@Serializable
data class Signature(
    @Contextual val signature: Binary,
    @SerialName("pub_key") val publicKey: PublicKey,
    @SerialName("account_number") @Serializable(ULongAsStringSerializer::class) val accountNumber: ULong? = null,
    @Serializable(ULongAsStringSerializer::class) val sequence: ULong? = null,
) {

    constructor(
        signature: Binary,
        publicKey: Binary,
        accountNumber: ULong? = null,
        sequence: ULong? = null,
        publicKeyType: String = "tendermint/PubKeySecp256k1",
    ) : this(
        signature,
        PublicKey(publicKey, publicKeyType),
        accountNumber,
        sequence,
    )
}