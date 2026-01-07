package ag.sportradar.mobile.player.data.sdk.session

import ag.sportradar.mobile.player.data.sdk.session.channel.PlaybackChannel
import kotlinx.coroutines.flow.SharedFlow

/**
 * Default implementation of [PlaybackSession].
 *
 * This class:
 * - Manages multiple [PlaybackChannel] instances
 * - Tracks the currently active playback channel
 * - Subscribes to channel state and media progress changes
 * - Exposes session-level state updates via [SharedFlow]
 *
 * @property provider Provides playback assets from a given stream URL.
 * @property playerViewModel The ViewModel that coordinates playback logic and coroutine scope.
 */
internal class DefaultPlaybackSession(
    defaultChannel: PlaybackChannel,
    additionalChannels: List<PlaybackChannel>
) : PlaybackSession {

//    private val playbackChannels: Map<String, PlaybackChannel> = mutableMapOf(
//        defaultChannel.channelId to defaultChannel
//    ).apply {
//        additionalChannels.forEach { channel ->
//            this[channel.channelId] = channel
//        }
//    }
//
//    private var activeChannelId: String = defaultChannel.channelId
//    private var streamUrl: String? = null
//
//    /**
//     * SharedFlow emitting session-level playback states (e.g., PLAYING, PAUSED, ENDED).
//     * Replays the latest state and has extra buffer capacity for burst updates.
//     */
//    private val _playbackStates = MutableSharedFlow<PlaybackSessionState>(
//        replay = 1,
//        extraBufferCapacity = 32
//    )
//
//    /**
//     * SharedFlow emitting media progress updates (e.g., current position, duration).
//     * Replays the latest progress event and buffers burst updates.
//     */
//    private val _mediaProgressStates = MutableSharedFlow<ChannelProgressState>(
//        replay = 1,
//        extraBufferCapacity = 32
//    )
//
//    override val playbackStates: SharedFlow<PlaybackSessionState> =
//        _playbackStates.asSharedFlow()
//
//    override val mediaProgressStates: SharedFlow<ChannelProgressState> =
//        _mediaProgressStates.asSharedFlow()
//
//
//
//    /**
//     * Loads a playback asset for the provided [streamUrl].
//     *
//     * @param streamUrl Must be a non-blank HTTPS URL.
//     * @throws IllegalArgumentException if the URL is invalid.
//     */
//    override fun startSession(streamUrl: String) {
//        this.streamUrl = streamUrl
//
//        getPlaybackChannel(activeChannelId)?.prepareChannel(streamUrl)
//    }
//
//    /**
//     * Returns a [PlaybackChannel] by its ID, or falls back to the active channel if [channelId] is null.
//     *
//     * @param channelId Optional channel identifier.
//     * @return The corresponding [PlaybackChannel], or `null` if not found.
//     */
//    override fun getPlaybackChannel(channelId: String?): PlaybackChannel? =
//        if (channelId != null) playbackChannels[channelId] else activeChannelId?.let { playbackChannels[it] }
//
//    /**
//     * Sets the active playback channel.
//     *
//     * @param channelId The ID of an existing playback channel.
//     */
//    override fun setActivePlaybackChannel(channelId: String) {
//        if (playbackChannels.containsKey(channelId)) activeChannelId = channelId
//    }
//
//    /** Starts playback on the given channel or the active one if [channelId] is null. */
//    override fun play(channelId: String?) = operate(channelId) { this.play() }
//
//    /** Pauses playback on the given channel or the active one if [channelId] is null. */
//    override fun pause(channelId: String?) = operate(channelId) { this.pause() }
//
//    /** Seeks playback on the given channel or the active one if [channelId] is null. */
//    override fun seek(channelId: String?) = operate(channelId) { this.seek() }
//
//    /** Rewinds playback on the given channel or the active one if [channelId] is null. */
//    override fun rewind(channelId: String?) = operate(channelId) { this.rewind() }
//
//    /**
//     * Executes a channel-specific action.
//     *
//     * @param channelId Optional channel ID. Defaults to the active channel if null.
//     * @param action The operation to execute on the channel.
//     */
//    private fun operate(channelId: String?, action: PlaybackChannel.() -> Unit) {
//        getPlaybackChannel(channelId = channelId ?: activeChannelId)?.action()
//    }
//
//    /**
//     * Subscribes to playback state and media progress updates from a channel.
//     *
//     * Runs inside [viewModelScope] for structured concurrency.
//     *
//     * @param playbackChannel The channel to observe.
//     */
////    private fun subscribeToStateChanges(playbackChannel: PlaybackChannel) {
////        playerViewModel.viewModelScope.launch {
////            launch { subscribeToPlaybackStateChanges(playbackChannel) }
////            launch { subscribeToMediaProgressChanges(playbackChannel) }
////        }
////    }
//
//    /**
//     * Collects playback state changes from a [PlaybackChannel] and emits them as [PlaybackSessionState].
//     *
//     * @param playbackChannel The channel to observe.
//     */
//    private suspend fun subscribeToPlaybackStateChanges(playbackChannel: PlaybackChannel) {
//        playbackChannel.state.collect { state ->
//            emitState(
//                PlaybackSessionState(
//                    state = state,
//                    channelId = playbackChannel.channelId
//                )
//            )
//
//
//            when (state) {
//                PlaybackChannelState.ENDED -> {
//                    // Optional: cleanup logic (e.g., auto-detach, resource release)?
//                }
//
//                is PlaybackChannelState.ERROR -> {
//                    // Optional: cleanup logic (e.g., auto-detach, resource release)?
//                    throw (state.throwable)
//                }
//
//                else -> Napier.d("PlaybackSessionState (${playbackChannel.channelId}): $state")
//
//            }
//        }
//    }
//
//    /**
//     * Collects media progress updates from a [PlaybackChannel].
//     *
//     * Currently no-op, but can be extended to emit progress updates via [_mediaProgressStates].
//     *
//     * @param playbackChannel The channel to observe.
//     */
//    private suspend fun subscribeToMediaProgressChanges(playbackChannel: PlaybackChannel) {
//        playbackChannel.mediaProgress.collect { state ->
//            emitMediaProgress(state)
//            Napier.d("MediaProgressState (${playbackChannel.channelId}): $state")
//        }
//    }
//
//    /**
//     * Emits a playback session state into [_playbackStates].
//     *
//     * @param state The state to emit.
//     */
//    private fun emitState(state: PlaybackSessionState) {
//        _playbackStates.tryEmit(state)
//    }
//
//    /**
//     * Emits a media-progress state into [_mediaProgressStates].
//     *
//     * @param mediaProgress The state to emit.
//     */
//    private fun emitMediaProgress(mediaProgress: ChannelProgressState) {
//        _mediaProgressStates.tryEmit(mediaProgress)
//    }
}