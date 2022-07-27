kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxCoroutineVersion: String by project
                val kotlinxSerializationVersion: String by project

                api(project(":"))
                api(project(":proto-serializers"))
                api(project(":client-grpc"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("kr.jadekim:j-logger:2.0.3-alpha4")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("kr.jadekim:j-logger-slf4j:2.0.3-alpha4")
            }
        }
    }
}
