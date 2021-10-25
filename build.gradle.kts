plugins {
    kotlin("multiplatform") version "1.5.30"
    id("maven-publish")
}

group = "money.terra"
version = "0.20.1"

allprojects {
    apply {
        plugin("kotlin-multiplatform")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
    }

    group = rootProject.group
    version = rootProject.version

    kotlin {
        jvm {
            compilations.all {
                kotlinOptions.jvmTarget = "1.8"
            }
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }

        @Suppress("UNUSED_VARIABLE")
        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-common"))
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }
            val jvmMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
            val jvmTest by getting {
                dependencies {
                    val junitVersion: String by project

                    implementation(kotlin("test-junit5"))

                    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
                    compileOnly("org.junit.jupiter:junit-jupiter-api:$junitVersion")
                    compileOnly("org.junit.jupiter:junit-jupiter-params:$junitVersion")
                }
            }
        }
    }

    publishing {
        repositories {
            val ossrhUsername: String by project
            val ossrhPassword: String by project

            if (version.toString().endsWith("-SNAPSHOT", true)) {
                maven {
                    name = "mavenCentralSnapshot"
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            } else {
                maven {
                    name = "mavenCentral"
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            }
        }
    }
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxCoroutineVersion: String by project

                api(project(":types"))
                api(project(":wallet"))
                api(project(":messages"))
                api(project(":sdk-tools"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
            }
        }
    }
}
