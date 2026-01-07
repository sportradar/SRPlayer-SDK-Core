import ag.sportradar.plugins.gradle.GitVersionPlugin
import ag.sportradar.plugins.gradle.KoverUtilPlugin
import ag.sportradar.plugins.gradle.UnsafeUsage
import com.android.builder.core.BuilderConstants
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.co.touchlab.skie)
    alias(libs.plugins.com.codingfeline.buildkonfig)
    alias(libs.plugins.dev.icerock.mobile.multiplatform.resources)
    alias(libs.plugins.org.jetbrains.dokka)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.ag.sportradar.plugins.gradle.kover.util)
    alias(libs.plugins.ag.sportradar.plugins.gradle.pitest)
    alias(libs.plugins.org.sonarqube)
    id("maven-publish")
    id("com.autonomousapps.dependency-analysis")
}

skie {
    build {
        produceDistributableFramework()
    }
    features {
        defaultArgumentsInExternalLibraries.set(true)
        enableSwiftUIObservingPreview.set(true)
        enableFutureCombineExtensionPreview.set(false)
        enableFlowCombineConvertorPreview.set(false)
    }
}

@OptIn(UnsafeUsage::class)
fun customVersionNameBehaviour(): String {
    return if (System.getenv("CI_COMMIT_BRANCH") == "main") "abc"
    else GitVersionPlugin.getVersionName(format = "%t%_s.%c")
}

tasks.register("printCustomVersionName") {
    doLast {
        println("Version Name: ${customVersionNameBehaviour()}")
    }
}
@OptIn(UnsafeUsage::class)
val versionName = customVersionNameBehaviour()
val bundleVersion = versionName
val buildNumber = GitVersionPlugin.getVersionCode()

// ----- KMP -----

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }

    androidTarget {
        publishLibraryVariants(
            com.android.builder.core.BuilderConstants.DEBUG,
            com.android.builder.core.BuilderConstants.RELEASE
        )
        publishLibraryVariantsGroupedByFlavor = true
    }

    val iosFrameworkName = AppConfig.IOS_FRAMEWORK_NAME
    val xcf = XCFramework(iosFrameworkName)
    val iosCfg: KotlinNativeTarget.() -> Unit = {
        binaries {
            framework {
                binaryOption("bundleId", AppConfig.IOS_BUNDLE_ID_PROD)
                baseName = iosFrameworkName
                // The build version that identifies an iteration of the bundle.
                // This key is a machine-readable string composed of one to three period-separated integers, such as 10.14.1.
                // The string can only contain numeric characters (0-9) and periods.
                // You can include more integers but the system ignores them.
                binaryOption("bundleVersion", buildNumber.toString())
                // The release or version number of the bundle.
                // This key is a user-visible string for the version of the bundle.
                // The required format is three period-separated integers, such as 10.14.1.
                // The string can only contain numeric characters (0-9) and periods.
                binaryOption("bundleShortVersionString", bundleVersion)

                export("dev.icerock.moko:resources:${libs.versions.moko.get()}")

                xcf.add(this)
            }
        }
    }
    iosArm64(iosCfg)
    iosX64(iosCfg)
    iosSimulatorArm64(iosCfg)

    jvm()

    // Export KDocs into generated Obj-C framework headers
    targets.withType<KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions.freeCompilerArgs.add("-Xexport-kdoc")
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting
        val androidMain by getting
        val jvmMain by getting

        val jvmAndAndroidMain by creating {
            dependsOn(commonMain)
        }

        androidMain.dependsOn(jvmAndAndroidMain)
        jvmMain.dependsOn(jvmAndAndroidMain)

        commonMain.dependencies {
            implementation(compose.runtime) // for @Composable annotations
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            // resources
            api(libs.dev.icerock.moko.resources)

            // coroutines
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)

            // viewmodel
            implementation(libs.androidx.lifecycle.lifecycle.viewmodel)

            // networking - ktor
            implementation(project.dependencies.platform(libs.io.ktor.ktor.bom))
            implementation("io.ktor:ktor-client-core")
            implementation("io.ktor:ktor-client-json")
            implementation("io.ktor:ktor-client-serialization")
            implementation("io.ktor:ktor-client-logging")
            implementation("io.ktor:ktor-client-content-negotiation")
            implementation("io.ktor:ktor-http")
            implementation("io.ktor:ktor-serialization")
            implementation("io.ktor:ktor-serialization-kotlinx-json")
            implementation("io.ktor:ktor-utils")

            // serialization
            implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.core)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)

            // dependency injection
            api(libs.io.insert.koin.koin.core)

            // logging
            implementation(libs.io.github.aakira.napier)

            // native time
            implementation(libs.com.soywiz.korlibs.klock.klock)

            // skie
            implementation(libs.co.touchlab.skie.configuration.annotations)
        }
        androidMain.dependencies {
            implementation(libs.io.ktor.ktor.client.okhttp)
            implementation(libs.org.slf4j.slf4j.simple)
        }
        jvmMain.dependencies {
            implementation(libs.io.ktor.ktor.client.okhttp)
            implementation(libs.org.slf4j.slf4j.simple)
        }
        iosMain.dependencies {
            implementation(libs.io.ktor.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.junit.junit)
            implementation(libs.org.jetbrains.kotlin.kotlin.test)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)
            implementation(libs.io.insert.koin.koin.test)
            implementation(libs.io.ktor.ktor.client.mock)
            // implementation(libs.io.mockk.mockk.common)
        }
        jvmTest.dependencies {
            implementation(libs.io.mockk.mockk)

            implementation(libs.androidx.datastore.datastore)
            implementation(libs.androidx.datastore.datastore.core)
            implementation(libs.androidx.datastore.datastore.preferences)
            implementation(libs.androidx.datastore.datastore.preferences.core)
        }

        all {
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCRefinement")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlin.io.encoding.ExperimentalEncodingApi")
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
            AppConfig.KOTLIN_LANG.takeIf { it.isNotBlank() }
                ?.let { languageSettings.languageVersion = it }

            @Suppress("OPT_IN_USAGE")
            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
            languageSettings.enableLanguageFeature("WhenGuards")
        }
    }
}

// ----- ANDROID -----

android {
    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
            withSourcesJar()
        }
    }

    namespace = AppConfig.APP_NAMESPACE
    compileSdk = AppConfig.COMPILE_SDK_VERSION
    buildToolsVersion = AppConfig.BUILD_TOOLS_VERSION
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AppConfig.MIN_SDK_VERSION
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    buildTypes {
        maybeCreate(BuilderConstants.DEBUG).apply {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        maybeCreate(BuilderConstants.RELEASE).apply {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


// ----- BUILDKONFIG -----

fun isMainCIBranch(): Boolean =
    System.getenv("CI_COMMIT_BRANCH").let { it == "main" || it == "production" || it == "master" }

val calmBaseUrl: String =
    if (isMainCIBranch()) "cfd.staging.calm-nonprod.sportradar.dev" else "cfd.staging.calm-nonprod.sportradar.dev" // TODO: change prod url when available!

buildkonfig {
    packageName = AppConfig.BASE_PACKAGE_NAME
    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "USER_AGENT",
            value = "ag.sportradar.mobile.AVPlayerDataSDK/${GitVersionPlugin.getVersionName()} (KMP; ${GitVersionPlugin.getVersionCode()})",
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "CALM_BASE_URL",
            value = calmBaseUrl,
        )
    }
}

// ----- MOKO RESOURCES -----

// multiplatform resources go in the `commonMain/moko-resources` folder
// any changes need to be applied by running the following gradle task (regenerate bindings)
// ./gradlew generateMRcommonMain
multiplatformResources {
    resourcesPackage.set(AppConfig.BASE_PACKAGE_NAME) // required
    iosBaseLocalizationRegion.set("en") // optional, default "en"
}

// ----- KOVER -----
val coverageExclusions = listOf(
    "**.*$\$serializer", // generated serializer code
    "${AppConfig.BASE_PACKAGE_NAME}.BuildKonfig", // generated build config
    "${AppConfig.BASE_PACKAGE_NAME}.MR*", // generated resources
    "${AppConfig.BASE_PACKAGE_NAME}.test.*", // unit tests
    "${AppConfig.BASE_PACKAGE_NAME}.injection.*", // dependency injection
    "${AppConfig.BASE_PACKAGE_NAME}.storage.CreateDataStore*", // expect actual functions for datastore platform instantiation
    "${AppConfig.BASE_PACKAGE_NAME}.model.*", // models should be pure objects for the most part
    "${AppConfig.BASE_PACKAGE_NAME}.vm.state.*", // state should be pure objects for the most part
    "${AppConfig.BASE_PACKAGE_NAME}.util.JwtToken*",
    "${AppConfig.BASE_PACKAGE_NAME}.AvPlayerDataSdkInitializer*", // this is very platform dependant and can't really be tested
)

koverUtil {
    excludedClasses.addAll(coverageExclusions)
}

pitest {
    targetClasses.add("${AppConfig.BASE_PACKAGE_NAME}.*")
    avoidCallsTo.add("io.github.aakira.napier")
    excludedClasses.addAll(
        "${AppConfig.BASE_PACKAGE_NAME}.injection.*",
    )
    verbose.set(true)
    useJUnit5Plugin.set(false)
}

// ----- DOKKA -----
dokka {
    moduleName.set("av-player-data-sdk")
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
    }
    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(
                setOfNotNull(
                    VisibilityModifier.Public,
                    VisibilityModifier.Protected,
                ),
            )
            skipDeprecated.set(false)
            reportUndocumented.set(true)
            skipEmptyPackages.set(true)
            jdkVersion.set(libs.versions.java.get().toInt())
            enableKotlinStdLibDocumentationLink.set(true)
            enableJdkDocumentationLink.set(true)
            enableAndroidDocumentationLink.set(true)
            includes.from(layout.projectDirectory.file("PackageDocs.md"))
            val gitProjectPath = System.getenv("CI_PROJECT_PATH")
            val branch = System.getenv("CI_COMMIT_BRANCH")
            if (branch != null && gitProjectPath != null) {
                sourceLink {
                    localDirectory.set(file("$projectDir/src"))
                    remoteUrl.set(uri("https://gitlab.sportradar.ag/$gitProjectPath/-/tree/$branch/${project.name}/src"))
                }
            }
        }
    }
}

// ----- PUBLISHING -----
publishing {
    publications {
        all {
            group = AppConfig.MAVEN_GROUP
            version = versionName
        }
    }
    repositories {
        maven {
            url =
                uri("https://gitlab.sportradar.ag/api/v4/projects/${System.getenv("CI_PROJECT_ID")}/packages/maven")
            name = "GitLab"
            credentials(PasswordCredentials::class.java) {
                username = "gitlab-ci-token"
                password = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("basic", BasicAuthentication::class)
            }
        }
    }
}

// ----- SONARQUBE -----
val projectKey = "mobile-app-av-player-data-sdk-kmp"
val hostUrl = "https://sonarqube.sportradar.ag"
val projectName = "AV Player Data SDK"
val srcDirs: Set<File> =
    files(
        "$projectDir/src/commonMain",
        "$projectDir/src/androidMain",
        "$projectDir/src/iosMain",
        "$projectDir/src/jvmMain",
    ).files.filter { it.exists() }.toSet()
val testDirs: Set<File> =
    files(
        "$projectDir/src/commonTest",
        "$projectDir/src/androidUnitTest",
        "$projectDir/src/iosTest",
        "$projectDir/src/jvmTest",
    ).files.filter { it.exists() }.toSet()
val coverageReport = "$projectDir/build/reports/kover/project-xml/report.xml"

sonarqube {
    properties {
        property("sonar.host.url", hostUrl)
        property("sonar.projectKey", projectKey)
        property("sonar.sources", srcDirs)
        property("sonar.tests", testDirs)
        property("sonar.projectName", projectName)
        property("sonar.coverage.jacoco.xmlReportPaths", coverageReport)
        property(
            "sonar.exclusions",
            "build/**," +
                    "bin/**," +
                    "gradle/**," +
                    "gradlew," +
                    "gradlew.bat," +
                    "*.gradle",
        )
        property("sonar.scm.provider", "git")
        property("sonar.verbose", "false")
        property("sonar.buildString", versionName)
        property("sonar.analysis.versionName", versionName)
        property("sonar.analysis.versionCode", buildNumber)
        property(
            "sonar.coverage.exclusions",
            KoverUtilPlugin.fileGlobsOf(coverageExclusions).joinToString(",")
        )
    }
}

tasks.matching { it.name in listOf("explodeCodeSourceDebug", "explodeCodeSourceRelease") }
    .configureEach {
        dependsOn("generateActualResourceCollectorsForAndroidMain")
    }
