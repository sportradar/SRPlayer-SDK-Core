package ag.sportradar.mobile.player.data.sdk.utils

/**
 * Provides platform-specific information for the player SDK.
 *
 * @property isAndroid Indicates if the current platform is Android.
 */
expect class PlatformInfo() {
    val isAndroid: Boolean
}