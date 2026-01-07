package ag.sportradar.mobile.player.data.sdk.session

import ag.sportradar.mobile.player.data.sdk.session.channel.PlaybackChannel

/**
 * Represents a playback session that can manage multiple [PlaybackChannel]s,
 * handle playback controls, and expose session-level state and progress updates.
 */
interface PlaybackSession {

//    /**
//     * Flow emitting session-level playback states (e.g., PLAYING, PAUSED, ENDED).
//     *
//     * Observers can collect this flow to react to playback state changes.
//     */
//    val playbackStates: Flow<PlaybackSessionState>
//
//    /**
//     * Flow emitting media progress updates (e.g., current position, duration).
//     *
//     * Observers can collect this flow to track media progress.
//     */
//    val mediaProgressStates: Flow<ChannelProgressState>
//
//    /**
//     * Loads a playback asset for the provided stream URL.
//     *
//     * @param streamUrl A valid HTTPS URL pointing to the media asset.
//     * @throws IllegalArgumentException if the URL is invalid.
//     */
//    fun startSession(streamUrl: String)
//
//    /**
//     * Returns a [PlaybackChannel] by its ID, or the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     * @return The corresponding [PlaybackChannel], or `null` if no channel matches the ID or session has no channels.
//     */
//    fun getPlaybackChannel(channelId: String? = null): PlaybackChannel?
//
//    /**
//     * Sets the active playback channel for the session.
//     *
//     * @param channelId The ID of an existing playback channel.
//     */
//    fun setActivePlaybackChannel(channelId: String)
//
//    /**
//     * Starts playback on the specified channel, or the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     */
//    fun play(channelId: String? = null)
//
//    /**
//     * Pauses playback on the specified channel, or the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     */
//    fun pause(channelId: String? = null)
//
//    /**
//     * Seeks playback on the specified channel, or the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     */
//    fun seek(channelId: String? = null)
//
//    /**
//     * Rewinds playback on the specified channel, or the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     */
//    fun rewind(channelId: String? = null)
}