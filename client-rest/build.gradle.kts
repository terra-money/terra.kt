plugins {
    kotlin("plugin.serialization") version "1.6.0"
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                val kotlinxSerializationVersion: String by project
                val ktorVersion: String by project
                val commonUtilVersion: String by project

                implementation(project(":types"))
                implementation(project(":messages"))

                api(project(":amino-serializers"))

                compileOnly(project(":sdk-tools"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                implementation("kr.jadekim:common-util:$commonUtilVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                val ktorVersion: String by project

                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
    }
}
