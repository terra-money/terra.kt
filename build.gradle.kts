plugins {
    kotlin("multiplatform") version "1.6.21"
    id("org.jetbrains.dokka") version "1.6.20"
    id("maven-publish")
    id("signing")
}

group = "money.terra"
version = "0.30.0-alpha1"

allprojects {
    apply {
        plugin("kotlin-multiplatform")
        plugin("org.jetbrains.dokka")
        plugin("maven-publish")
        plugin("signing")
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    group = rootProject.group
    version = rootProject.version

    kotlin {
        jvm {
            compilations.all {
                kotlinOptions.jvmTarget = "11"
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

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set(project.name)
                description.set("Kotlin, Java SDK for Terra")
                url.set("https://github.com/jdekim43/kotlin-common")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("jdekim43")
                        name.set("Jade Kim")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/terra-money/terra.kt.git")
                    developerConnection.set("scm:git:git://github.com/terra-money/terra.kt.git")
                    url.set("https://github.com/terra-money/terra.kt")
                }
            }
        }

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

    signing {
        sign(publishing.publications)
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
