plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

javafx {
    version = "21.0.7"
    modules("javafx.controls", "javafx.web", "javafx.fxml")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "ch.documedis.handson.App"
}

// Copy webview dist into resources before processing
tasks.named<Copy>("processResources") {
    // If webview was pre-built, include it
    val webviewDist = file("webview/dist")
    if (webviewDist.exists()) {
        from(webviewDist) {
            into("webview")
        }
    }
}
