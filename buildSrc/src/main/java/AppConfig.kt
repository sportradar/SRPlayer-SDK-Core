@Suppress("MemberVisibilityCanBePrivate")
object AppConfig {
    // iOS framework name
    const val IOS_FRAMEWORK_NAME = "AVPlayerDataSDK"

    // Base package name
    const val BASE_PACKAGE_NAME = "ag.sportradar.mobile.player.data.sdk"

    // Maven group
    const val MAVEN_GROUP = BASE_PACKAGE_NAME

    // Android namespace
    const val APP_NAMESPACE = BASE_PACKAGE_NAME
    const val SAMPLE_APP_NAMESPACE = "${APP_NAMESPACE}.android"

    // Android app ID (the base ID that may be additionally suffixed for different build types)
    const val APP_BASE_ID = APP_NAMESPACE
    const val SAMPLE_APP_BASE_ID = SAMPLE_APP_NAMESPACE

    // Environment suffixes (used in app IDs, file providers, dynamic links)
    const val PROD_SUFFIX = ""

    // Environment types (used in build types and everything that entails, e.g. build dirs, gradle tasks)
    const val PROD_ENV = "prod"

    // Android versioning
    const val MIN_SDK_VERSION = 29
    const val TARGET_SDK_VERSION = 36
    const val COMPILE_SDK_VERSION = TARGET_SDK_VERSION
    const val BUILD_TOOLS_VERSION = "36.0.0"

    // iOS framework bundle identifiers
    const val IOS_BUNDLE_ID_PROD = "${BASE_PACKAGE_NAME}${PROD_SUFFIX}.${IOS_FRAMEWORK_NAME}"

    // used for enabling experimental kotlin features (this is not the java version)
    const val KOTLIN_LANG = "" // set to blank to use default
}
