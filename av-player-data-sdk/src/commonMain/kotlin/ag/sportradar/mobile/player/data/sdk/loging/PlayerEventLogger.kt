package ag.sportradar.mobile.player.data.sdk.loging

import ag.sportradar.mobile.player.data.sdk.SRAVPlayerConfiguration
import ag.sportradar.mobile.player.data.sdk.loging.PlayerLoggingProvider.Companion.TAG
import ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack
import ag.sportradar.mobile.player.data.sdk.player.state.TrackState

/**
 * A centralized logging utility for player-related events.
 *
 * This object provides a set of standardized functions to log various aspects
 * of the player's lifecycle and behavior, such as state changes, errors, network activity,
 * and user interactions. It delegates actual logging to underlying logger implementations
 * (e.g., `PlayerLoggerImpl`, `SRAVLogger`) to ensure consistent log formatting and output throughout the SDK.
 *
 * Usage examples include:
 * - Tracking playback state transitions (e.g., Play -> Pause).
 * - monitoring network request performance.
 * - Debugging track selection and availability changes.
 */
object PlayerEventLogger {

    private val loggerConfiguration = SRAVPlayerConfiguration.instance.loggerConfig

    val playerLoggerImpl = PlayerLoggerImpl(loggerConfiguration)

    // Core logging methods delegating to playerLoggerImpl
    fun logError(error: Throwable, context: String = "") {
        playerLoggerImpl.error(
            "Player error${if (context.isNotEmpty()) " in $context" else ""}: ${error.message}",
            throwable = error
        )
    }

    fun logWarning(message: String, tag: String = TAG, throwable: Throwable? = null) {
        playerLoggerImpl.warning(message, tag, throwable)
    }

    fun logInfo(message: String, tag: String = TAG, throwable: Throwable? = null) {
        playerLoggerImpl.info(message, tag, throwable)
    }

    fun logDebug(message: String, tag: String = TAG, throwable: Throwable? = null) {
        playerLoggerImpl.debug(message, tag, throwable)
    }

    fun logVerbose(message: String, tag: String = TAG, throwable: Throwable? = null) {
        playerLoggerImpl.verbose(message, tag, throwable)
    }

    // Event-specific logging methods

    fun logNetworkRequest(url: String, method: String = "GET") {
        playerLoggerImpl.debug("Network request: $method $url")
    }

    fun logNetworkResponse(url: String, statusCode: Int, duration: Long) {
        playerLoggerImpl.debug("Network response: $url - Status: $statusCode, Duration: ${duration}ms")
    }

    fun logNetworkRequestFailure(url: String, exception: Throwable) {
        playerLoggerImpl.error(
            "Network request failed for $url: ${exception.message}",
            throwable = exception
        )
    }

    fun logJsonParsingError(message: String, exception: Throwable) {
        playerLoggerImpl.error(
            "JSON parsing error: $message",
            throwable = exception
        )
    }

    fun logNetworkException(message: String, exception: Throwable) {
        playerLoggerImpl.error(
            "Network exception: $message",
            throwable = exception
        )
    }

    fun logTracksChanged(tracks: TrackState) {
        playerLoggerImpl.debug("Video tracks changed: ${tracks.videoTracks.forEach { it }}")
        playerLoggerImpl.debug("Audio tracks changed: ${tracks.audioTracks.forEach { it }}")
        playerLoggerImpl.debug("Subtitle tracks changed: ${tracks.subtitleTracks.forEach { it }}")
    }

}
