plugins {
  kotlin("jvm") version "1.7.10"
  kotlin("plugin.serialization") version "1.7.10"
}

val kotlin_version: String by project
val ktor_version: String by project
dependencies {
  implementation(project(":core"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}