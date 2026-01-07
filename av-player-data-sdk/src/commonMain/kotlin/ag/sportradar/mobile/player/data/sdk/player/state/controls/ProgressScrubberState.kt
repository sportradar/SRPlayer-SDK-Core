package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the UI state of the progress scrubber in player controls.
 *
 * @property durationMs Total duration of the media in milliseconds.
 * @property bufferedPosition Current buffered position in milliseconds.
 * @property currentPositionMs Current playback position in milliseconds.
 * @property isLive Indicates if the stream is live.
 * @property enabled Indicates if the scrubber is interactive.
 * @property isSeekable Indicates if seeking is allowed.
 * @property currentUserSeekPosition The user's seek position as a normalized value (0.0 to 1.0), or null if unset.
 */
data class ProgressScrubberState(
    val durationMs: Long,
    val bufferedPosition: Long,
    val currentPositionMs: Long,
    val isLive: Boolean,
    val enabled: Boolean,
    val isSeekable: Boolean,
    private val currentUserSeekPosition: Float? = null
) {
    /**
     * The current seekbar position as a value between 0 and 1.
     */
    val seekbarPosition: Float
        get() = if (durationMs > 0) {
            (currentPositionMs.coerceIn(0, durationMs).toFloat() / durationMs)
        } else 0f

    /**
     * Returns the user's seek position as a normalized value between 0.0 and 1.0.
     *
     * Clamps the value to the valid range. Returns null if no seek position is set.
     */
    fun getCurrentUserSeekPosition(): Float? = currentUserSeekPosition?.let {
        when {
            it > 1f -> 1f
            it < 0f -> 0f
            else -> it
        }
    }

    /**
     * Returns the total duration as a string in mm:ss format.
     */
    fun durationString(): String = durationMs.positionToString()

    /**
     * Returns the current playback position as a string in mm:ss format.
     */
    fun currentPositionString(): String = currentPositionMs.positionToString()

    /**
     * Returns the user's current seek position as a string in mm:ss format.
     *
     * If [currentUserSeekPosition] is set, calculates the position in ms and formats it.
     * Otherwise, returns "No seeking".
     */
    fun currentUserSeekPositionString(): String = currentUserSeekPosition?.let {
        (durationMs * it).toLong().positionToString()
    } ?: "No seeking"
}

/**
 * Converts a duration in milliseconds to a string in mm:ss format.
 *
 * @receiver Duration in milliseconds.
 * @return Formatted time string (mm:ss).
 */
fun Long.positionToString(): String {
    val totalSeconds = (this / 1000).toInt()
    val minutes = (totalSeconds / 60).toString().padStart(2, '0')
    val seconds = (totalSeconds % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}