allprojects {
    apply(plugin = "dependency-config-convention")
}

plugins {
    base
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.ag.sportradar.plugins.gradle.git.version) version "1.0.111"
    alias(libs.plugins.ag.sportradar.plugins.gradle.health.check)
}

healthCheck {
    lintReportXml.set("build/reports/lint-results-${AppConfig.PROD_ENV}.xml")
    javaHomeCheck.set(false)

    configureDependencyAnalysis.set {
        it.issues {
            all {
                onUsedTransitiveDependencies {
                    exclude(
                        "org.jetbrains.kotlin:kotlin-stdlib",
                        "io.github.aakira:napier-android-debug",
                    )
                }
                onIncorrectConfiguration {
                    exclude(
                        "org.jetbrains.kotlin:kotlin-stdlib",
                    )
                }
            }
        }
    }
}

tasks.getByName<Delete>("clean") {
    doLast {
        allprojects.forEach { p ->
            delete(p.layout.buildDirectory)
        }
    }
}
