package ag.sportradar.mobile.player.data.sdk.player

/**
 * Interface defining basic playback controls for a media player.
 */
interface PlayerControllable {

    /**
     * Toggles between play and pause states.
     */
    fun playOrPause()

    /**
     * Resumes media playback if paused.
     */
    fun play()

    /**
     * Pauses media playback if playing.
     */
    fun pause()

    /**
     * Seeks to the specified position in seconds.
     */
    fun seekTo(positionS: Long)

    /**
     * Seeks by the specified offset in seconds.
     */
    fun seekBy(offsetS: Long)

    /**
     * Seeks to the live position in a live stream.
     */
    fun seekToLive()
}