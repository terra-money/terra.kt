package money.terra.client.rest.api

import kotlinx.coroutines.Deferred
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.client.rest.model.Result
import money.terra.model.PublicKey
import money.terra.model.TypeWrapper
import money.terra.type.ULongAsStringSerializer

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
interface AuthApi {

    fun getAccountInfo(address: String): Deferred<Result<TypeWrapper<AccountInfo?>>>
}

@Serializable
@SerialName("core/Account")
data class AccountInfo(
    val address: String,
    @SerialName("account_number") @Serializable(ULongAsStringSerializer::class) val accountNumber: ULong,
    @SerialName("public_key") val publicKey: PublicKey? = null,
    @Serializable(ULongAsStringSerializer::class) val sequence: ULong = 0u,
)