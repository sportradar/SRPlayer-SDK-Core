package ag.sportradar.mobile.player.data.sdk.session.channel

/**
 * Holds the progress information for a playback channel's player.
 *
 * This state includes:
 * - [progressMs]: The current playback position in milliseconds.
 * - [durationMs]: The total duration of the media in milliseconds.
 * - [bufferProgressMs]: The buffered position in milliseconds, indicating how much of the media is loaded.
 *
 * Useful for updating UI elements such as progress bars, buffer indicators, and time displays.
 *
 * @property progressMs Current playback position in milliseconds.
 * @property durationMs Total duration of the media in milliseconds.
 * @property bufferProgressMs Buffered position in milliseconds.
 */
data class ChannelProgressState(
    val progressMs: Long,
    val durationMs: Long,
    val bufferProgressMs: Long
)