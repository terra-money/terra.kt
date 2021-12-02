package money.terra.sdk.tools.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.model.PublicKey

interface AccountInfoProvider {

    suspend fun get(walletAddress: String): AccountInfo

    suspend fun increaseSequence(walletAddress: String)

    suspend fun refreshSequence(walletAddress: String)
}

@Serializable
data class AccountInfo(
    val address: String,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long = 0,
    @SerialName("public_key") val publicKey: PublicKey? = null,
    @Serializable(LongAsStringSerializer::class) val sequence: Long = 0,
)