package ag.sportradar.mobile.player.data.sdk.session.channel

import ag.sportradar.mobile.player.data.sdk.asset.PlaybackAsset
import ag.sportradar.mobile.player.data.sdk.player.PlayerControllable
import ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack
import ag.sportradar.mobile.player.data.sdk.player.state.TrackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines a playback channel that can be controlled and observed for media playback.
 *
 * A playback channel encapsulates the logic for controlling media playback and exposing its state.
 * It extends [PlayerControllable] to provide standard playback operations such as play, pause, seek, and rewind.
 *
 * Key properties:
 * - [isReadyState]: Emits whether the channel is ready for playback.
 * - [playerState]: Emits the current playback state (e.g., playing, paused, buffering, ended, error).
 * - [progressState]: Emits progress information (current position, duration, buffer progress).
 * - [channelId]: Unique identifier for the channel instance.
 * - [isStreamLive]: Indicates whether the current stream is a live broadcast.
 *
 * Implementations should update these flows to reflect the current state of the underlying player.
 */
interface PlaybackChannel : PlayerControllable {
    /**
     * Emits whether the channel is ready for playback.
     *
     * Observers can collect this flow to determine when playback can begin.
     */
    val isReadyState: StateFlow<Boolean>

    /**
     * Emits the current playback state of the channel.
     *
     * Observers can collect this flow to react to state changes such as playing, paused, buffering, ended, or error.
     */
    val playerState: StateFlow<ChannelPlayerState>

    /**
     * Emits the current state of audio/video tracks available and selected within the channel.
     *
     * Observers can collect this flow to receive updates on available tracks (e.g., video qualities, audio languages)
     * and which specific track is currently active. This is crucial for UI elements that allow track selection.
     */
    val trackState: StateFlow<TrackState>

    /**
     * Emits progress information for the current media.
     *
     * Observers can collect this flow to track playback position, duration, and buffer progress.
     */
    val progressState: StateFlow<ChannelProgressState>

    /**
     * Unique identifier for this playback channel instance.
     */
    val channelId: String

    /**
     * Emits the current playback type indicating whether the stream is live or on-demand.
     *
     * Observers can collect this flow to determine if the content being played is a live broadcast
     * or video-on-demand (VOD), which may affect UI elements like seek bar behavior or live indicators.
     */
    val isStreamLive: StateFlow<PlaybackType>

    /**
     * Prepares the playback channel with the specified asset.
     *
     * @param asset The playback asset to be loaded and prepared for playback.
     */
    fun prepareChannel(asset: PlaybackAsset)

    /**
     * Resets the playback channel to its initial state.
     *
     * Implementations should release resources, clear buffers, and prepare the channel for reuse.
     * This is typically called when switching streams or reinitializing playback.
     */
    fun resetChannel()

    /**
     * Destroys the playback channel and releases all resources.
     *
     * Implementations should perform final cleanup, release player resources, and stop any ongoing playback.
     * This is typically called when the channel is no longer needed and should not be reused.
     */
    fun destroyChannel()

    /**
     * Updates the player state of the channel.
     *
     * @param state The new player state to set.
     */
    fun updatePlayerState(state: ChannelPlayerState)

    /**
     * Selects the specified track for playback.
     *
     * Implementations should switch the current media track to the provided [track].
     * This may involve updating the player configuration and notifying observers of the change.
     *
     * @param track The [SRAVTrack] to be selected for playback.
     */
    fun selectTrack(track: SRAVTrack)
}