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
        val jvmTest by getting {
            dependencies {
                implementation("money.terra:proto-kotlin:0.5.16")
                implementation("money.terra:grpc-kotlin:0.5.16")
                val junitVersion: String by project

                implementation(kotlin("test-junit5"))

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
                compileOnly("org.junit.jupiter:junit-jupiter-api:$junitVersion")
                compileOnly("org.junit.jupiter:junit-jupiter-params:$junitVersion")
            }
        }
    }
}
