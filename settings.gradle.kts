pluginManagement {
    repositories {
        // Gradle plugins
        maven("https://gitlab.sportradar.ag/api/v4/projects/9799/packages/maven")

        gradlePluginPortal()
        mavenCentral()
        google()

        mavenLocal()
    }
}

plugins {
    // resolve and download java when the version declared in build scripts does not match the installed version
    id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()

        // Version catalog
        maven("https://gitlab.sportradar.ag/api/v4/projects/10351/packages/maven")
        // Epsilon
        maven("https://gitlab.sportradar.ag/api/v4/projects/9863/packages/maven")
        // Mosaic
        maven("https://gitlab.sportradar.ag/api/v4/projects/12385/packages/maven")

        // compose/kotlin dev builds
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")

        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("ag.sportradar.gradle:version-catalog:1.0.222")
        }
        create("local") {
            from(files("gradle/local.versions.toml"))
        }
    }
}

rootProject.name = "AV Player Data SDK"
include(":av-player-data-sdk")
