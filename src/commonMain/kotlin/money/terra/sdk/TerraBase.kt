package money.terra.sdk

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import money.terra.key.MnemonicKey.Companion.COIN_TYPE
import money.terra.model.TransactionResult
import money.terra.sdk.tools.transaction.broadcaster.BroadcastResult
import money.terra.sdk.tools.transaction.broadcaster.Broadcaster
import money.terra.sdk.wallet.ConnectedTerraWallet
import money.terra.wallet.TerraWallet
import kotlin.coroutines.CoroutineContext

abstract class TerraBase(
    val chainId: String,
    val broadcaster: Broadcaster<out BroadcastResult>,
) {

    abstract fun connect(wallet: TerraWallet): ConnectedTerraWallet

    fun walletFromAddress(address: String) = connect(TerraWallet(address))

    fun walletFromRawKey(
        privateKey: ByteArray,
        publicKey: ByteArray? = null,
    ) = connect(TerraWallet.fromRawKey(privateKey, publicKey))

    fun walletFromRawKey(
        privateKey: String,
        publicKey: String? = null,
    ) = connect(TerraWallet.fromRawKey(privateKey, publicKey))

    fun walletFromMnemonic(
        mnemonic: String,
        account: Int = 0,
        index: Int = 0,
        coinType: Int = COIN_TYPE,
    ) = connect(TerraWallet.fromMnemonic(mnemonic, account, index, coinType))

    abstract fun getTransaction(
        transactionHash: String,
    ): Deferred<TransactionResult?>

    open fun waitTransaction(
        transactionHash: String,
        intervalMillis: Long = 1000,
        initialMillis: Long = 6000,
        maxCheckCount: Int? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ) = broadcaster.wait(
        transactionHash,
        intervalMillis,
        initialMillis,
        maxCheckCount,
        coroutineContext,
    )
}
