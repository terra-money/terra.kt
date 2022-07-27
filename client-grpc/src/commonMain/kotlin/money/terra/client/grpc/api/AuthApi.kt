package money.terra.client.grpc.api

import kotlinx.coroutines.Deferred
import money.terra.sdk.tools.transaction.AccountInfo

interface AuthApi {

    fun getAccountInfo(address: String): Deferred<AccountInfo?>
}
