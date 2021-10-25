kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxCoroutineVersion: String by project
                val kotlinxSerializationVersion: String by project

                api(project(":"))
                api(project(":amino-serializers"))
                api(project(":client-rest"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                val ktorVersion: String by project

                implementation("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
    }
}
