package money.terra.client.grpc.provider

import money.terra.client.grpc.TerraGrpcClient
import money.terra.sdk.tools.transaction.AccountInfo
import money.terra.sdk.tools.transaction.AccountInfoProvider

class AlwaysFetchAccountInfoProvider(private val client: TerraGrpcClient) : AccountInfoProvider {

    override suspend fun get(walletAddress: String): AccountInfo? {
        return client.authApi.getAccountInfo(walletAddress).await()
    }

    override suspend fun increaseSequence(walletAddress: String) {
        //do nothing
    }

    override suspend fun refreshSequence(walletAddress: String) {
        //do nothing
    }
}

abstract class CachedAccountInfoProvider(
    private val client: TerraGrpcClient,
) : AccountInfoProvider {

    abstract suspend fun getCached(walletAddress: String): AccountInfo?

    abstract suspend fun setCache(walletAddress: String, accountInfo: AccountInfo)

    override suspend fun get(walletAddress: String): AccountInfo? {
        var cached = getCached(walletAddress)

        if (cached == null) {
            cached = client.authApi.getAccountInfo(walletAddress).await() ?: return null
            setCache(walletAddress, cached)
        }

        return cached
    }

    override suspend fun increaseSequence(walletAddress: String) {
        getCached(walletAddress)?.let {
            setCache(walletAddress, it.copy(sequence = it.sequence + 1))
        }
    }
}

class LocalCachedAccountInfoProvider(
    client: TerraGrpcClient,
) : CachedAccountInfoProvider(client) {

    private val data = mutableMapOf<String, AccountInfo>()

    override suspend fun getCached(walletAddress: String): AccountInfo? = data[walletAddress]

    override suspend fun setCache(walletAddress: String, accountInfo: AccountInfo) {
        data[walletAddress] = accountInfo
    }

    override suspend fun refreshSequence(walletAddress: String) {
        data.clear()
    }
}