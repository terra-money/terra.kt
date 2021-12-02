package money.terra.client.rest.api

import kotlinx.coroutines.Deferred
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.client.rest.model.Result
import money.terra.model.PublicKey
import money.terra.model.TypeWrapper

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface AuthApi {

    fun getAccountInfo(address: String): Deferred<Result<TypeWrapper<AccountInfo?>>>
}

@Serializable
@SerialName("core/Account")
data class AccountInfo(
    val address: String,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long,
    @SerialName("public_key") val publicKey: PublicKey? = null,
    @Serializable(LongAsStringSerializer::class) val sequence: Long = 0,
)