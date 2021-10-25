package money.terra.util

expect object Ripemd160 {

    fun hash(data: ByteArray): ByteArray
}