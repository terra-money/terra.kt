package money.terra.client.grpc

import io.grpc.Channel
import kotlinx.coroutines.Dispatchers
import money.terra.client.grpc.TerraGrpcClient.Companion.DEFAULT_PORT
import money.terra.client.grpc.api.AuthApi
import money.terra.client.grpc.api.AuthApiJvm
import money.terra.client.grpc.api.TransactionApi
import money.terra.client.grpc.api.TransactionApiJvm
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

actual fun TerraGrpcClient(
    host: String,
    port: Int,
    coroutineContext: CoroutineContext?,
): TerraGrpcClient = TerraGrpcClientJvm(DefaultTerraGrpcChannel(host, port), coroutineContext ?: Dispatchers.Default)

class TerraGrpcClientJvm constructor(
    channel: Channel,
    override val coroutineContext: CoroutineContext = Dispatchers.Default,
) : TerraGrpcClient {

    constructor(name: String, port: Int = DEFAULT_PORT) : this(DefaultTerraGrpcChannel(name, port))

    override val authApi: AuthApi = AuthApiJvm(this)
    override val transactionApi: TransactionApi = TransactionApiJvm(this)

    val cosmos = CosmosGrpcClient(channel)

    class CosmosGrpcClient(channel: Channel) {
        val authQuery = cosmos.auth.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val authzMsg = cosmos.authz.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val authzQuery = cosmos.authz.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val bankMsg = cosmos.bank.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val bankQuery = cosmos.bank.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val tendermintService = cosmos.base.tendermint.v1beta1.ServiceGrpcKt.ServiceCoroutineStub(channel)
        val crisisMsg = cosmos.crisis.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val distributionMsg = cosmos.distribution.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val distributionQuery = cosmos.distribution.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val evidenceMsg = cosmos.evidence.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val evidenceQuery = cosmos.evidence.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val feegrantMsg = cosmos.feegrant.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val feegrantQuery = cosmos.feegrant.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val govMsg = cosmos.gov.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val govQuery = cosmos.gov.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val mintQuery = cosmos.mint.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val paramsQuery = cosmos.params.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val slashingMsg = cosmos.slashing.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val slashingQuery = cosmos.slashing.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val stakingMsg = cosmos.staking.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
        val stakingQuery = cosmos.staking.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val txService = cosmos.tx.v1beta1.ServiceGrpcKt.ServiceCoroutineStub(channel)
        val upgradeQuery = cosmos.upgrade.v1beta1.QueryGrpcKt.QueryCoroutineStub(channel)
        val vestingMsg = cosmos.vesting.v1beta1.MsgGrpcKt.MsgCoroutineStub(channel)
    }

    val cosmwasm = CosmWasmGrpcClient(channel)

    class CosmWasmGrpcClient(channel: Channel) {
        val wasmMsg = cosmwasm.wasm.v1.MsgGrpcKt.MsgCoroutineStub(channel)
    }

    val ibc = IbcGrpcClient(channel)

    class IbcGrpcClient(channel: Channel) {
        val applications = ApplicationsGrpcClient(channel)

        class ApplicationsGrpcClient(channel: Channel) {
            val feeMsg = ibc.applications.fee.v1.MsgGrpcKt.MsgCoroutineStub(channel)
            val feeQuery = ibc.applications.fee.v1.QueryGrpcKt.QueryCoroutineStub(channel)
            val transferMsg = ibc.applications.transfer.v1.MsgGrpcKt.MsgCoroutineStub(channel)
            val transferQuery = ibc.applications.transfer.v1.QueryGrpcKt.QueryCoroutineStub(channel)

            val interchainAccount = InterChainAccountsGrpcClient(channel)

            class InterChainAccountsGrpcClient(channel: Channel) {
                val controllerQuery =
                    ibc.applications.interchain_accounts.controller.v1.QueryGrpcKt.QueryCoroutineStub(channel)
                val hostQuery = ibc.applications.interchain_accounts.host.v1.QueryGrpcKt.QueryCoroutineStub(channel)
            }
        }

        val core = CoreGrpcClient(channel)

        class CoreGrpcClient(channel: Channel) {
            val channelMsg = ibc.core.channel.v1.MsgGrpcKt.MsgCoroutineStub(channel)
            val channelQuery = ibc.core.channel.v1.QueryGrpcKt.QueryCoroutineStub(channel)
            val clientMsg = ibc.core.client.v1.MsgGrpcKt.MsgCoroutineStub(channel)
            val clientQuery = ibc.core.client.v1.QueryGrpcKt.QueryCoroutineStub(channel)
            val connectionMsg = ibc.core.connection.v1.MsgGrpcKt.MsgCoroutineStub(channel)
            val connectionQuery = ibc.core.connection.v1.QueryGrpcKt.QueryCoroutineStub(channel)
        }
    }

    val tendermint = TendermintGrpcClient(channel)

    class TendermintGrpcClient(channel: Channel) {
        val abciApplication = tendermint.abci.ABCIApplicationGrpcKt.ABCIApplicationCoroutineStub(channel)
    }
}