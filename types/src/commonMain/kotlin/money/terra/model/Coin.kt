package money.terra.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import money.terra.type.Uint128
import money.terra.type.Decimal
import kotlin.jvm.JvmStatic

@Serializable
data class Coin(
    val amount: Uint128,
    @SerialName("denom") val denomination: String,
) {

    @Suppress("FunctionName")
    companion object {
        // @formatter:off
        @JvmStatic fun LUNA(amount: Uint128) = Coin(amount, "uluna")
        @JvmStatic fun AUT(amount: Uint128) = Coin(amount, "uaud")
        @JvmStatic fun CAT(amount: Uint128) = Coin(amount, "ucad")
        @JvmStatic fun CHT(amount: Uint128) = Coin(amount, "uchf")
        @JvmStatic fun CNT(amount: Uint128) = Coin(amount, "ucny")
        @JvmStatic fun DKT(amount: Uint128) = Coin(amount, "udkk")
        @JvmStatic fun EUT(amount: Uint128) = Coin(amount, "ueur")
        @JvmStatic fun GBT(amount: Uint128) = Coin(amount, "ugbp")
        @JvmStatic fun HKT(amount: Uint128) = Coin(amount, "uhkd")
        @JvmStatic fun IDT(amount: Uint128) = Coin(amount, "uidr")
        @JvmStatic fun INT(amount: Uint128) = Coin(amount, "uinr")
        @JvmStatic fun JPT(amount: Uint128) = Coin(amount, "ujpy")
        @JvmStatic fun KRT(amount: Uint128) = Coin(amount, "ukrw")
        @JvmStatic fun MNT(amount: Uint128) = Coin(amount, "umnt")
        @JvmStatic fun MYT(amount: Uint128) = Coin(amount, "umyr")
        @JvmStatic fun NOT(amount: Uint128) = Coin(amount, "unok")
        @JvmStatic fun PHT(amount: Uint128) = Coin(amount, "uphp")
        @JvmStatic fun SDT(amount: Uint128) = Coin(amount, "usdr")
        @JvmStatic fun SET(amount: Uint128) = Coin(amount, "usek")
        @JvmStatic fun SGT(amount: Uint128) = Coin(amount, "usgd")
        @JvmStatic fun THT(amount: Uint128) = Coin(amount, "uthb")
        @JvmStatic fun TWT(amount: Uint128) = Coin(amount, "utwd")
        @JvmStatic fun UST(amount: Uint128) = Coin(amount, "uusd")
        // @formatter:on
    }

    constructor(denomination: String, amount: Uint128) : this(amount, denomination)

    override fun toString(): String = amount.toString() + denomination
}

@Serializable
data class CoinDecimal(
    val amount: Decimal,
    @SerialName("denom") val denomination: String,
) {

    @Suppress("FunctionName")
    companion object {
        // @formatter:off
        @JvmStatic fun LUNA(amount: Decimal) = CoinDecimal(amount, "uluna")
        @JvmStatic fun AUT(amount: Decimal) = CoinDecimal(amount, "uaud")
        @JvmStatic fun CAT(amount: Decimal) = CoinDecimal(amount, "ucad")
        @JvmStatic fun CHT(amount: Decimal) = CoinDecimal(amount, "uchf")
        @JvmStatic fun CNT(amount: Decimal) = CoinDecimal(amount, "ucny")
        @JvmStatic fun DKT(amount: Decimal) = CoinDecimal(amount, "udkk")
        @JvmStatic fun EUT(amount: Decimal) = CoinDecimal(amount, "ueur")
        @JvmStatic fun GBT(amount: Decimal) = CoinDecimal(amount, "ugbp")
        @JvmStatic fun HKT(amount: Decimal) = CoinDecimal(amount, "uhkd")
        @JvmStatic fun IDT(amount: Decimal) = CoinDecimal(amount, "uidr")
        @JvmStatic fun INT(amount: Decimal) = CoinDecimal(amount, "uinr")
        @JvmStatic fun JPT(amount: Decimal) = CoinDecimal(amount, "ujpy")
        @JvmStatic fun KRT(amount: Decimal) = CoinDecimal(amount, "ukrw")
        @JvmStatic fun MNT(amount: Decimal) = CoinDecimal(amount, "umnt")
        @JvmStatic fun MYT(amount: Decimal) = CoinDecimal(amount, "umyr")
        @JvmStatic fun NOT(amount: Decimal) = CoinDecimal(amount, "unok")
        @JvmStatic fun PHT(amount: Decimal) = CoinDecimal(amount, "uphp")
        @JvmStatic fun SDT(amount: Decimal) = CoinDecimal(amount, "usdr")
        @JvmStatic fun SET(amount: Decimal) = CoinDecimal(amount, "usek")
        @JvmStatic fun SGT(amount: Decimal) = CoinDecimal(amount, "usgd")
        @JvmStatic fun THT(amount: Decimal) = CoinDecimal(amount, "uthb")
        @JvmStatic fun TWT(amount: Decimal) = CoinDecimal(amount, "utwd")
        @JvmStatic fun UST(amount: Decimal) = CoinDecimal(amount, "uusd")
        // @formatter:on
    }

    constructor(denomination: String, amount: Decimal) : this(amount, denomination)

    override fun toString(): String = amount.toString() + denomination
}
