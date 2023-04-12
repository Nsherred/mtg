plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

val ktor_version: String by project
val kotlin_version: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:2.1.0")
    implementation("io.ktor:ktor-client-serialization-jvm:2.1.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}