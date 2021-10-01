# Terra Kotlin SDK
The Terra Software Development Kit (SDK) in Kotlin is a simple library toolkit for building software that can interact
with the Terra blockchain and provides simple abstractions over core data structures, serialization, and API request generation.

> 🚧 Under heavy construction! 🚧 \
terra.kt is refactoring for applying protobuf and grpc.
Currently, you can only use legacy amino format with lcd.

## Features
* Wallet Tools
* Amino
  * LCD, FCD Client
  * Sign with amino format
* Tools for server

### Backlog
* protobuf and grpc
* Support another platform (iOS / Android)
* Multisig
* Remote signature (e.g. Hashicorp Vault plugin)

## Installation (w/ Gradle)
```
dependencies {
    implementation("money.terra:sdk-amino:$terraSdkVersion")
    
    // or
    
    implementation("money.terra:wallet:$terraSdkVersion")
    implementation("money.terra:messages:$terraSdkVersion")
    implementation("money.terra:client-rest:$terraSdkVersion")
    implementation("money.terra:sdk-tools:$terraSdkVersion")
}
```

## Usage
### Getting blockchain data
```
dependencies {
    implementation("money-terra:client-rest:$terraSdkVersion")
}
```
```
val client: TerraRestClient = TerraLcdClient("bombay-12", "https://bombay-lcd.terra.dev")

val deferredResult: Deferred<Result<Coin>> = client.marketApi.estimateSwapResult(Uint128("10000"), "uluna", "ukrw")
val result: Coin = deferredResult.await().result

println("uluna can be swapped for $result")
```
### Broadcasting transactions
```
dependencies {
    implementation("money.terra:sdk-amino:$terraSdkVersion")
}
```
```
    val mnemonic: String = "..."
    val terra = Terra.fcd("bombay-12", "https://bombay-fcd.terra.dev")
    val wallet: TerraWallet = terra.walletFromMnemonic(mnemonic)
    val receiveWallet: TerraWallet = TerraWallet("terra1x46rqay4d3cssq8gxxvqz8xt6nwlz4td20k38v")
    
    // If use TerraFcdClient, You can broadcast transaction without fee parameter.
    // It will be estimate fee using fcd.
    val deferredResult: Deferred<Pair<BroadcastResult, Transaction>> = wallet.broadcast {
        SendMessage(wallet.address, receiveWallet, listOf(Coin(Uint128(1000), "uluna)).withThis()
    }
    
    // or
    
    val transaction: Transaction = Transaction.builder()
        .message(SendMessage(wallet.address, receiveWallet, listOf(Coin(Uint128(1000), "uluna)))
        .fee(Fee(200000u, listOf(Coin(Uint128("50"), "uluna"))))
        .build()
    val deferredResult: Deferred<Pair<BroadcastResult, Transaction>> = wallet.broadcast(transaction)
    
    val (result, signedTransaction) = deferredResult.await()
    val isSucceedBroadcast = result.isSuccess
    val transactionHash = result.transactionHash
```
### Signing transactions
```
dependencies {
    implementation("money.terra:wallet:$terraSdkVersion")
    implementation("money.terra:sdk-amino:$terraSdkVersion") {
      exclude("money.terra", "client-rest")
    }
}
```
```
val mnemonic = "..."
val wallet = TerraWallet.fromMnemonic(mnemonic)
val signData = TransactionSignData(
    chainId = "bombay-12",
    accountNumber = 0u,
    sequence = 0u,
    fee = Fee(200000u, listOf(Coin(Uint128("50"), "uluna"))),
    messages = listOf(SendMessage(wallet.address, receiveWallet, listOf(Coin(Uint128(1000), "uluna))),
    memo = "",
)

val signature: Signature = AminoTransactionSigner.sign(wallet, signData)
val signatureBytes: ByteArray = signature.signature.data
```
