package ag.sportradar.mobile.player.data.sdk.utils

import platform.Foundation.NSBundle

actual fun getApplicationId(context: CommonContext): String {
    return ""
}

actual fun getBundleId(): String {
    return NSBundle.mainBundle.bundleIdentifier ?: ""
}

actual fun getCertificateFingerprint(context: CommonContext): String {
    return ""
}
