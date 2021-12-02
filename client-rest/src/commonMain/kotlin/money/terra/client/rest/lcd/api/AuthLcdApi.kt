package money.terra.client.rest.lcd.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.SerializationException
import money.terra.client.rest.api.AccountInfo
import money.terra.client.rest.api.AuthApi
import money.terra.client.rest.model.Result
import money.terra.client.rest.HttpClient
import money.terra.model.TypeWrapper

@Deprecated("Legacy endpoint will deprecate. Use new proto style endpoints.")
class AuthLcdApi(
    private val client: HttpClient,
) : AuthApi {

    override fun getAccountInfo(address: String): Deferred<Result<TypeWrapper<AccountInfo?>>> =
        CoroutineScope(Dispatchers.Unconfined).async {
            try {
                client.get<Result<TypeWrapper<AccountInfo?>>>("/auth/accounts/$address").await()
            } catch (e: SerializationException) {
                if (e.message?.endsWith("missing") == true) {
                    return@async Result(0, TypeWrapper("core/Account", null))
                }

                throw e
            }
        }
}