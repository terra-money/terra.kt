package money.terra.sdk.tools.transaction

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer
import money.terra.key.Key
import money.terra.model.*
import money.terra.type.Binary
import money.terra.type.Uint128
import money.terra.wallet.TerraWallet

interface TransactionSigner {

    val signMode: SignMode

    suspend fun sign(key: Key, data: TransactionSignDocument): ByteArray
}

@Serializable
data class TransactionSignDocument(
    val body: TransactionBody,
    val authInfo: AuthInfo,
    val chainId: String,
    val accountNumber: Long,
)

@Serializable
@Deprecated("Legacy format")
data class TransactionSignData(
    @SerialName("chain_id") val chainId: String,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long,
    @Serializable(LongAsStringSerializer::class) val sequence: Long,
    val fee: Fee,
    @SerialName("msgs") val messages: List<@Contextual Message>,
    val memo: String,
) {

    constructor(
        transaction: StdTx,
        chainId: String,
        accountNumber: Long,
        sequence: Long,
    ) : this(
        chainId,
        accountNumber,
        sequence,
        transaction.fee!!,
        transaction.messages,
        transaction.memo,
    )
}
