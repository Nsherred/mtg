@file:Suppress("PropertyName")

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val graphql_java_version: String by project

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

dependencies {
    implementation(project(":ecs"))
    implementation(project(":core"))
    implementation("com.github.haifengl:smile-kotlin:2.6.0")
    implementation(group="com.github.haifengl", name="smile-mkl", version="2.6.0")
    implementation("com.github.haifengl:smile-plot:2.6.0")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation(kotlin("reflect"))
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.ktor:ktor-client-cio-jvm:2.1.0")
    implementation("io.ktor:ktor-client-serialization-jvm:2.1.0")

    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation(kotlin("test"))
    // These are needed for regression i Smile.

//    implementation(group="org.bytedeco", name="javacpp", version="1.5.3", classifier="macosx-x86_64")
//    implementation(group="org.bytedeco", name="openblas", version="0.3.9-1.5.3", classifier="macosx-x86_64")
//    implementation(group="org.bytedeco", name="arpack-ng", version="3.7.0-1.5.3", classifier="macosx-x86_64")
}

tasks.test {
    minHeapSize = "512m"
    maxHeapSize = "2048m"
    jvmArgs = listOf("-XX:MaxPermSize=512m")
    useJUnitPlatform()
}