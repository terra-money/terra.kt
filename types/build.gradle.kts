plugins {
    kotlin("plugin.serialization") version "1.5.30"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxSerializationVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }
    }
}
