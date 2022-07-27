plugins {
    kotlin("plugin.serialization") version "1.6.21"
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
                val terraProtoVersion: String by project
                val kotlinxSerializationVersion: String by project
                val commonUtilVersion: String by project

                api(project(":types"))
                api(project(":messages"))

                api("money.terra:proto-kotlin:$terraProtoVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

                implementation("kr.jadekim:common-encoder:$commonUtilVersion")
            }
        }
    }
}
