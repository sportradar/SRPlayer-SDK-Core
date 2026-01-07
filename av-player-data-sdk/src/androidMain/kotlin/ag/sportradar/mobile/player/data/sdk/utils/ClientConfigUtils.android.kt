package ag.sportradar.mobile.player.data.sdk.utils

import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

actual fun getApplicationId(context: CommonContext): String {
    PlayerEventLogger.logInfo("Application ID set to: ${context.packageName}")
    val applicationId: String = context.packageName
    return applicationId
}

actual fun getBundleId(): String {
    return ""
}

actual fun getCertificateFingerprint(context: CommonContext): String {
    val algorithm = "SHA-256"
    val packageName = context.packageName
    val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val signingInfo = context.packageManager
            .getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            .signingInfo
        if (signingInfo?.hasMultipleSigners() == true) {
            signingInfo.apkContentsSigners
        } else {
            signingInfo?.signingCertificateHistory
        }
    } else {
        @Suppress("DEPRECATION")
        context.packageManager
            .getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            .signatures
    }

    val signature = signatures?.firstOrNull() ?: return ""
    val md = MessageDigest.getInstance(algorithm)
    val digest = md.digest(signature.toByteArray())

    val fingerprint = digest.joinToString(":") { "%02X".format(it) }

    PlayerEventLogger.logInfo("Fingerprint ID set to: $fingerprint")

    return fingerprint
}
