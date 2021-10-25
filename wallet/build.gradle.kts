kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxCoroutineVersion: String by project
                val commonUtilVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

                implementation("kr.jadekim:common-util:$commonUtilVersion")
                implementation("kr.jadekim:common-hash:$commonUtilVersion")
                implementation("kr.jadekim:common-encoder:$commonUtilVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                val web3jVersion: String by project

                implementation("org.web3j:crypto:$web3jVersion")
            }
        }
    }
}
