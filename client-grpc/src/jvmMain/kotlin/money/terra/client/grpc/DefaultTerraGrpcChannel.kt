package money.terra.client.grpc

import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import money.terra.client.grpc.TerraGrpcClient.Companion.DEFAULT_PORT

@Suppress("FunctionName")
fun DefaultTerraGrpcChannel(name: String, port: Int = DEFAULT_PORT): Channel = ManagedChannelBuilder.forAddress(name, port)
    .usePlaintext()
    .build()