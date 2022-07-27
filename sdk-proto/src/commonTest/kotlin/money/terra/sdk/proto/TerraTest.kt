package money.terra.sdk.proto

import kr.jadekim.logger.JLog
import money.terra.client.grpc.TerraGrpcClient
import money.terra.message.SendMessage
import money.terra.model.*
import money.terra.proto.ProtobufFormat
import money.terra.sdk.proto.wallet.ProtoTransactionSigner
import money.terra.sdk.tools.transaction.StaticGasPricesProvider
import money.terra.sdk.tools.transaction.TransactionSignDocument
import money.terra.sdk.tools.transaction.broadcaster.BroadcastException
import money.terra.type.Binary
import money.terra.type.Decimal
import money.terra.type.Uint128
import util.runBlockingTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TerraTest {

    private val options = ProtoTerraOptions(
        chainId = "pisco-1",
        client = TerraGrpcClient("archive.terra.testnet.brzlabs.cc"),
        gasPricesProvider = StaticGasPricesProvider(mapOf("uluna" to Decimal("0.15"))),
    )

    init {
        JLog.get("test").debug("testlog")
    }

    @AfterTest
    fun delay() {
        Thread.sleep(1000) //Delay each test for public node syncing
    }

    @Test
    fun broadcastAsync() = runBlockingTest {
        val terra = ProtoTerra.proto(options.asyncBroadcaster())
        val wallet = terra.walletFromRawKey("008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6")

        val (broadcastResult, transaction) = wallet.broadcast {
            SendMessage(wallet.address, wallet.address, listOf(Coin("uluna", Uint128("1")))).withThis()
        }.await()
        println(broadcastResult)
        println(transaction)
        assertTrue(broadcastResult.code == 0)

        val transactionResult = terra.waitTransaction(broadcastResult.transactionHash, 200, 4000, 100).await()
        println(transactionResult)
        assertTrue(transactionResult.code == 0)
    }

    @Test
    fun broadcastSync() = runBlockingTest {
        val terra = ProtoTerra.proto(options.syncBroadcaster())
        val wallet = terra.walletFromRawKey("008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6")

        val (broadcastResult, transaction) = wallet.broadcast {
            SendMessage(wallet.address, wallet.address, listOf(Coin("uluna", Uint128("1")))).withThis()
        }.await()
        println(broadcastResult)
        println(transaction)
        assertTrue(broadcastResult.code == 0)

        val transactionResult = terra.waitTransaction(broadcastResult.transactionHash, 1000, 5000, 100).await()
        println(transactionResult)
        assertTrue(transactionResult.code == 0)
    }

    @Test
    fun broadcastBlock() = runBlockingTest {
        val terra = ProtoTerra.proto(options.blockBroadcaster())
        val wallet = terra.walletFromRawKey("008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6")

        val (broadcastResult, transaction) = wallet.broadcast {
            SendMessage(wallet.address, wallet.address, listOf(Coin("uluna", Uint128("1")))).withThis()
        }.await()
        println(broadcastResult)
        println(transaction)
        assertTrue(broadcastResult.code == 0)

        val transactionResult = terra.waitTransaction(broadcastResult.transactionHash, 200, 4000, 100).await()
        println(transactionResult)
        assertTrue(transactionResult.code == 0)
    }

    @Test
    fun failedTransaction() = runBlockingTest {
        val terra = ProtoTerra.proto(options.syncBroadcaster())
        val wallet = terra.walletFromRawKey("008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6")

        assertFailsWith(BroadcastException.FailedEstimateFee::class) {
            wallet.broadcast {
                SendMessage(wallet.address, wallet.address, listOf(Coin("ulu", Uint128("1")))).withThis()
            }.await()
        }

        val (broadcastResult, transaction) = wallet.broadcast(gasAmount = 100000) {
            SendMessage(wallet.address, wallet.address, listOf(Coin("ulu", Uint128("1")))).withThis()
        }.await()
        println(broadcastResult)
        println(transaction)
        assertTrue(broadcastResult.code == 0)

        val transactionResult = terra.waitTransaction(broadcastResult.transactionHash, 200, 4000, 100).await()
        println(transactionResult)
        assertTrue(transactionResult.code != 0)
    }

    @Test
    fun broadcastLcd() = runBlockingTest {
        val terra = ProtoTerra.proto(options.syncBroadcaster())
        val wallet = terra.walletFromRawKey("008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6")
        val accountInfo = wallet.getAccountInfo().await()!!
        val gasAmount = 200000L
        val gasPrice = CoinDecimal(Decimal("0.15"), "uluna")

        val transactionBody = TransactionBody(
            listOf(SendMessage(wallet.address, wallet.address, listOf(Coin("uluna", Uint128("1")))))
        )
        val authInfo = AuthInfo(
            listOf(Signer(PublicKey.Secp256k1(Binary(wallet.key!!.publicKey)), SignMode.Direct, accountInfo.sequence)),
            Fee(gasAmount, gasPrice)
        )
        val signDocument = TransactionSignDocument(
            transactionBody,
            authInfo,
            terra.chainId,
            accountInfo.accountNumber,
        )
        val signature = ProtoTransactionSigner.sign(wallet.key!!, signDocument)
        val signatures = listOf(Binary(signature))
        val transaction = Transaction(transactionBody, authInfo, signatures)
        val transactionBytes = ProtobufFormat.encodeToByteArray(transaction)

        //TODO: lcd.broadcast(transactionBytes, "BROADCAST_MODE_SYNC")
    }
}