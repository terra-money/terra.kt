package money.terra.client.grpc

import kotlinx.coroutines.CoroutineScope
import money.terra.client.grpc.TerraGrpcClient.Companion.DEFAULT_PORT
import money.terra.client.grpc.api.AuthApi
import money.terra.client.grpc.api.TransactionApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface TerraGrpcClient : CoroutineScope {

    companion object {
        val DEFAULT_PORT: Int = 9090
    }

    val authApi: AuthApi
    val transactionApi: TransactionApi
}

expect fun TerraGrpcClient(
    host: String,
    port: Int = DEFAULT_PORT,
    coroutineContext: CoroutineContext? = null,
): TerraGrpcClient