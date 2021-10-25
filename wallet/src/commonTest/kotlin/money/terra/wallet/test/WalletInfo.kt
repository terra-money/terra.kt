package money.terra.wallet.test

data class WalletInfo(
    val address: String,
    val mnemonic: String,
    val privateKeyHex: String,
    val publicKeyHex: String,
    val publicKeyBech32: String,
)

val NORMAL = WalletInfo(
    address = "terra1p77u5j0sfsgd29myws20lflpgtnxypfvwnut3y",
    mnemonic = "display ivory apology tenant interest coin rug garage spread among outside focus lizard grape layer corn hen zoo luxury human response betray cable damp",
    privateKeyHex = "008FA566B8829B68B29CC35C1EFDED3E163448722117F7D35D32A336612FE166A6",
    publicKeyHex = "0216F68D632F26A06CA559C9F38A3A54C34D4FB1C108DD5168F32701706216CA96",
    publicKeyBech32 = "terrapub1addwnpepqgt0drtr9un2qm99t8yl8z362np56na3cyyd65tg7vnszurzzm9fvfu9sfa",
)

val ZERO_END_PRIVATE_KEY = WalletInfo(
    address = "terra14pzzku0ltxnwc5eh5w6fzv4y8e9nj6645humrl",
    mnemonic = "blanket load skull faith glow donor try metal crystal reunion tired canyon wing still wire defy erosion anxiety labor ivory tank bless fantasy typical",
    privateKeyHex = "004D284CDFC68CCD5E91CBA245E3F9C6156165AC7F92E24C161D990B25A0C5AA00",
    publicKeyHex = "02BE1834E13E4746625DDB8FABC50DB9AFE4A16C3EC0D1B35784F887726C1AB683",
    publicKeyBech32 = "terrapub1addwnpepq2lpsd8p8er5vcjamw86h3gdhxh7fgtv8mqdrv6hsnugwunvr2mgx4amne5",
)
