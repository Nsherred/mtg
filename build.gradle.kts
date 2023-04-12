allprojects {
    group = "io.nicronomicon.mtg"
    version = "0.0.1"
}


subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://raw.github.com/mbuse/mvn-repository/releases/") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}
