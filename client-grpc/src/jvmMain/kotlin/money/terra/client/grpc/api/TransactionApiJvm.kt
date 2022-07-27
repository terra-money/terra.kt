package money.terra.client.grpc.api

import com.google.protobuf.kotlin.toByteString
import cosmos.tx.v1beta1.ServiceOuterClass
import cosmos.tx.v1beta1.broadcastTxRequest
import cosmos.tx.v1beta1.getTxRequest
import cosmos.tx.v1beta1.simulateRequest
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.serialization.encodeToByteArray
import money.terra.client.grpc.TerraGrpcClientJvm
import money.terra.model.Transaction
import money.terra.model.TransactionResponse
import money.terra.proto.ProtobufFormat
import money.terra.proto.mapper.type.TransactionMapper.fromProtobuf
import money.terra.proto.mapper.type.TransactionMapper.toProtobuf
import money.terra.proto.mapper.type.TransactionResponseMapper.fromProtobuf

class TransactionApiJvm(private val client: TerraGrpcClientJvm) : TransactionApi {

    override fun simulate(transaction: Transaction): Deferred<SimulateResult> = client.async {
        client.cosmos.txService.simulate(simulateRequest {
            txBytes = transaction.toProtobuf().toByteString()
        }).let {
            SimulateResult(
                GasInfo(
                    it.gasInfo.gasUsed,
                    it.gasInfo.gasWanted,
                ),
            )
        }
    }

    override fun getByHash(transactionHash: String): Deferred<GetTransactionResponse?> = client.async {
        try {
            client.cosmos.txService.getTx(getTxRequest {
                hash = transactionHash
            }).let {
                GetTransactionResponse(
                    it.tx.fromProtobuf(),
                    it.txResponse.fromProtobuf(),
                )
            }
        } catch (e: StatusException) {
            if (e.status.code == Status.NOT_FOUND.code) {
                return@async null
            }

            throw e
        }
    }

    override fun broadcastAsync(transaction: Transaction): Deferred<TransactionResponse> = client.async {
        client.cosmos.txService.broadcastTx(broadcastTxRequest {
            txBytes = ProtobufFormat.encodeToByteArray(transaction).toByteString()
            mode = ServiceOuterClass.BroadcastMode.BROADCAST_MODE_ASYNC
        }).txResponse.fromProtobuf()
    }

    override fun broadcastSync(transaction: Transaction): Deferred<TransactionResponse> = client.async {
        client.cosmos.txService.broadcastTx(broadcastTxRequest {
            txBytes = ProtobufFormat.encodeToByteArray(transaction).toByteString()
            mode = ServiceOuterClass.BroadcastMode.BROADCAST_MODE_SYNC
        }).txResponse.fromProtobuf()
    }

    override fun broadcastBlock(transaction: Transaction): Deferred<TransactionResponse> = client.async {
        client.cosmos.txService.broadcastTx(broadcastTxRequest {
            txBytes = ProtobufFormat.encodeToByteArray(transaction).toByteString()
            mode = ServiceOuterClass.BroadcastMode.BROADCAST_MODE_BLOCK
        }).txResponse.fromProtobuf()
    }
}

