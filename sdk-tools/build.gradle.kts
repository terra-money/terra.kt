plugins {
    kotlin("plugin.serialization") version "1.6.21"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxSerializationVersion: String by project
                val kotlinxCoroutineVersion: String by project
                val commonUtilVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
                implementation("kr.jadekim:common-util:$commonUtilVersion")

                api(project(":types"))
                implementation(project(":wallet"))
            }
        }
        val jvmMain by getting {
            dependencies {
                val kotlinxCoroutineVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutineVersion")
            }
        }
    }
}
