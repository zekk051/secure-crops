pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    shared {
        versions(
//            "1.18.1",
            "1.19.1",
            "1.19.4",
            "1.20.1",
            "1.21.1",
            "1.21.2",
            "1.21.5"
        )
        vcsVersion = "1.20.1"
    }
    create(rootProject)
}

rootProject.name = "secure-crops"