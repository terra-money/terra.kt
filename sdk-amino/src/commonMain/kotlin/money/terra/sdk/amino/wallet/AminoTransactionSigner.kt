package money.terra.sdk.amino.wallet

import kotlinx.serialization.json.*
import money.terra.amino.AminoFormat
import money.terra.model.PublicKey
import money.terra.model.Signature
import money.terra.model.Transaction
import money.terra.sdk.tools.transaction.TransactionSignData
import money.terra.sdk.tools.transaction.TransactionSigner
import money.terra.type.toBinary
import money.terra.wallet.TerraWallet

object AminoTransactionSigner : TransactionSigner {

    suspend fun sign(
        wallet: TerraWallet,
        data: TransactionSignData,
    ): Signature {
        val key = wallet.key ?: throw IllegalArgumentException("${wallet.address} wallet have not key")
        val signature = key.sign(serializeSignData(data)).await()

        return Signature(signature.toBinary(), PublicKey(key.publicKey.toBinary()), data.accountNumber, data.sequence)
    }

    override suspend fun sign(
        wallet: TerraWallet,
        data: TransactionSignData,
        transaction: Transaction,
    ): Signature = sign(wallet, data)

    fun serializeSignData(data: TransactionSignData): String {
        return AminoFormat.encodeToJsonElement(TransactionSignData.serializer(), data)
            .sorted()
            .let { AminoFormat.encodeToString(JsonElement.serializer(), it) }
    }

    private fun JsonElement.sorted(): JsonElement {
        return when (this) {
            is JsonPrimitive -> this
            JsonNull -> this
            is JsonObject -> {
                val sortedMap = LinkedHashMap<String, JsonElement>()

                keys.sorted()
                    .forEach { key -> sortedMap[key] = getValue(key).sorted() }

                JsonObject(sortedMap)
            }
            is JsonArray -> JsonArray(map { it.sorted() })
        }
    }
}