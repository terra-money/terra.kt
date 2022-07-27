package money.terra.client.grpc.api

import com.google.protobuf.kotlin.unpack
import cosmos.auth.v1beta1.queryAccountRequest
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import money.terra.client.grpc.TerraGrpcClientJvm
import money.terra.client.grpc.util.unpackPublicKey
import money.terra.sdk.tools.transaction.AccountInfo

class AuthApiJvm(private val client: TerraGrpcClientJvm) : AuthApi {

    override fun getAccountInfo(address: String): Deferred<AccountInfo?> = client.async {
        val response = try {
            client.cosmos.authQuery.account(queryAccountRequest {
                this.address = address
            })
        } catch (e: StatusException) {
            if (e.status == Status.NOT_FOUND) {
                return@async null
            } else {
                throw e
            }
        }

        val result = response.account.unpack<cosmos.auth.v1beta1.Auth.BaseAccount>()



        AccountInfo(
            result.address,
            result.accountNumber,
            if (result.hasPubKey()) result.pubKey.unpackPublicKey() else null,
            result.sequence,
        )
    }
}
