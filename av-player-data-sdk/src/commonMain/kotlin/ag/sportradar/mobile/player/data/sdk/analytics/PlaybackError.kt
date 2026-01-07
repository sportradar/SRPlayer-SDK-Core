package ag.sportradar.mobile.player.data.sdk.analytics

/**
 * Playback error information.
 */
data class PlaybackError(
    val code: String,
    val message: String,
    val exception: Throwable? = null
)
