package ag.sportradar.mobile.player.data.sdk.exception

interface ChromeCastException : SRAVPlayerException

/**
 * Indicates a Chromecast-related error during playback.
 * This error typically occurs when the application fails to communicate with the Chromecast device.
 */
object ChromecastError : ChromeCastException {
    override val internalCode: Int = 502
    override val externalCode: Int = 502
    override val errorTitle: String = "Chromecast Error"
    override val userMessage: String = "A Chromecast error occurred during playback."
    override val errorMessage: String = "Failed to communicate with Chromecast device."
}
