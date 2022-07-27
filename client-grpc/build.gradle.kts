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
                val kotlinxSerializationVersion: String by project
                val kotlinxCoroutineVersion: String by project
                val commonUtilVersion: String by project

                implementation(project(":types"))
                implementation(project(":messages"))

                api(project(":proto-serializers"))

                compileOnly(project(":sdk-tools"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

                implementation("kr.jadekim:common-util:$commonUtilVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                val terraProtoVersion: String by project
                val grpcVersion: String by project

                api("money.terra:grpc-kotlin:$terraProtoVersion")
                runtimeOnly("io.grpc:grpc-netty:$grpcVersion")
                compileOnly("org.apache.tomcat:annotations-api:6.0.53")
            }
        }
    }
}
