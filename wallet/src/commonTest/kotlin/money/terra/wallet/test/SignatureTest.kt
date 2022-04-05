package money.terra.wallet.test

import kr.jadekim.common.encoder.encodeBase64
import money.terra.key.PublicKey
import money.terra.key.RawKey
import money.terra.wallet.TerraWallet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SignatureTest {

    @Test
    fun succeedSign() {
        val wallet = TerraWallet.fromRawKey(NORMAL.privateKeyHex)
        val signature = (wallet.key as RawKey).signSync("text")
        assertEquals(
            "XUS608ydbfZ/acnQdlJzFOflrmkv1IuqZlWS3O+ZUZZFC98LO2BiGUbfAv4rtN/uKrzGy8lERwWWNGC9opgjXA==",
            signature.encodeBase64(),
        )

        //Jvm 에서 오류나는 케이스
        //keyPair.sign 을 사용할 경우 발생함.
//        val wallet2 = OwnTerraWallet("")
//        assertEquals("terra1f8jxqy6l9lkflpf4w8ggedv66x38fn4fu2zsl8", wallet2.address)
//        assertEquals("", wallet2.privateKeyHex)
//        assertEquals("0303C0B9A721876DDF9AF65A76F790AAD3457866AC92B793F2477F953603933AE0", wallet2.publicKeyHex)
//
//        val text = """{"account_number":"2328016","chain_id":"columbus-4","fee":{"amount":[{"amount":"109642223","denom":"ukrw"}],"gas":"200000"},"memo":"SpendCash|affe6ac6-3bc3-42c2-95f4-b8ae9a885997","msgs":[{"type":"bank/MsgSend","value":{"amount":[{"amount":"1","denom":"uluna"}],"from_address":"terra12ru0w70t0m82j9u3ncn95uk4xyrwfnhmtfjc3c","to_address":"terra12ru0w70t0m82j9u3ncn95uk4xyrwfnhmtfjc3c"}},{"type":"bank/MsgSend","value":{"amount":[{"amount":"18000000000","denom":"ukrw"}],"from_address":"terra1f8jxqy6l9lkflpf4w8ggedv66x38fn4fu2zsl8","to_address":"terra1t4n4egc3kd0y4v9ulkymacwjq0zsj7sy008xy5"}}],"sequence":"0"}"""
//        val signature2 = wallet2.sign(text)
//        assertEquals("GNVWIm0c3PltoPXUoACXe86WFRIhrhqJw3HG24x+LOMAS+ukEFg9V1zZ7BL48WnYEgVI+az6JbhhHHkwNSZBrw==", signature2.signature)
//        assertEquals("AwPAuachh23fmvZadveQqtNFeGaskreT8kd/lTYDkzrg", signature2.publicKey.value)
    }

    @Test
    fun succeedVerify() {
        val wallet = TerraWallet.fromRawKey(NORMAL.privateKeyHex)

        assertTrue {
            wallet.key!!.verify(
                "text",
                "XUS608ydbfZ/acnQdlJzFOflrmkv1IuqZlWS3O+ZUZZFC98LO2BiGUbfAv4rtN/uKrzGy8lERwWWNGC9opgjXA=="
            )
        }
    }

    @Test
    fun succeedRecoverPublicKeyFromSignature() {
        val publicKey = PublicKey.recoverFromSignature(
            "text",
            "XUS608ydbfZ/acnQdlJzFOflrmkv1IuqZlWS3O+ZUZZFC98LO2BiGUbfAv4rtN/uKrzGy8lERwWWNGC9opgjXA==",
        )

        assertTrue {
            publicKey.verify(
                "text",
                "XUS608ydbfZ/acnQdlJzFOflrmkv1IuqZlWS3O+ZUZZFC98LO2BiGUbfAv4rtN/uKrzGy8lERwWWNGC9opgjXA==",
            )
        }

        val wallet = TerraWallet.fromRawKey(NORMAL.privateKeyHex)

        assertTrue {
            publicKey.publicKey.contentEquals(wallet.key?.publicKey)
        }
    }

    @Test
    fun failedVerify() {
        val wallet = TerraWallet.create().first

        assertFalse {
            wallet.key!!.verify(
                "text",
                "XUS608ydbfZ/acnQdlJzFOflrmkv1IuqZlWS3O+ZUZZFC98LO2BiGUbfAv4rtN/uKrzGy8lERwWWNGC9opgjXA=="
            )
        }
    }
}