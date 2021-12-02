package money.terra.sdk.wallet

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import money.terra.model.Transaction
import money.terra.sdk.TerraBase
import money.terra.sdk.tools.transaction.AccountInfo
import money.terra.wallet.TerraWallet
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmOverloads

abstract class ConnectedTerraWallet(val terra: TerraBase, val origin: TerraWallet) : TerraWallet by origin {

    @JvmOverloads
    open fun broadcast(
        transaction: Transaction,
        gasAmount: Long? = null,
        feeDenomination: String? = null,
        accountNumber: Long? = null,
        sequence: Long? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ) = terra.broadcaster.broadcast(
        this,
        transaction,
        gasAmount,
        feeDenomination,
        accountNumber,
        sequence,
        coroutineContext,
    )

    @JvmOverloads
    open fun broadcast(
        gasAmount: Long? = null,
        feeDenomination: String? = null,
        accountNumber: Long? = null,
        sequence: Long? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
        transactionBuilder: Transaction.Builder.() -> Unit,
    ) = broadcast(
        Transaction.builder().apply(transactionBuilder).build(),
        gasAmount,
        feeDenomination,
        accountNumber,
        sequence,
        coroutineContext,
    )

    abstract fun getAccountInfo(): Deferred<AccountInfo>
}