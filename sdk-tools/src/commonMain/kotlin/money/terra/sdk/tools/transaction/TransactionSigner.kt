package money.terra.sdk.tools.transaction

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.model.Fee
import money.terra.model.Message
import money.terra.model.Signature
import money.terra.model.Transaction
import money.terra.type.ULongAsStringSerializer
import money.terra.wallet.TerraWallet

interface TransactionSigner {

    suspend fun sign(wallet: TerraWallet, data: TransactionSignData, transaction: Transaction): Signature
}

@Serializable
data class TransactionSignData(
    @SerialName("chain_id") val chainId: String,
    @SerialName("account_number") @Serializable(ULongAsStringSerializer::class) val accountNumber: ULong,
    @Serializable(ULongAsStringSerializer::class) val sequence: ULong,
    val fee: Fee,
    @SerialName("msgs") val messages: List<@Contextual Message>,
    val memo: String,
) {

    constructor(
        transaction: Transaction,
        chainId: String,
        accountNumber: ULong,
        sequence: ULong,
    ) : this(
        chainId,
        accountNumber,
        sequence,
        transaction.fee!!,
        transaction.messages,
        transaction.memo,
    )
}
