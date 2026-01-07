package ag.sportradar.mobile.player.data.sdk.player

import IsolatedKoinComponent
import ag.sportradar.mobile.player.data.sdk.SRAVPlayerConfiguration
import ag.sportradar.mobile.player.data.sdk.analytics.PlaybackError
import ag.sportradar.mobile.player.data.sdk.analytics.StreamEvent
import ag.sportradar.mobile.player.data.sdk.analytics.UserAction
import ag.sportradar.mobile.player.data.sdk.asset.PlaybackAsset
import ag.sportradar.mobile.player.data.sdk.exception.AssetException
import ag.sportradar.mobile.player.data.sdk.exception.CustomException
import ag.sportradar.mobile.player.data.sdk.exception.LicenceException
import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException
import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack
import ag.sportradar.mobile.player.data.sdk.player.entities.computedDisplayName
import ag.sportradar.mobile.player.data.sdk.player.entities.removeDuplications
import ag.sportradar.mobile.player.data.sdk.player.state.controls.ControlUIState
import ag.sportradar.mobile.player.data.sdk.player.state.controls.PlayPauseButtonState
import ag.sportradar.mobile.player.data.sdk.player.state.controls.ProgressScrubberState
import ag.sportradar.mobile.player.data.sdk.player.state.controls.SettingType
import ag.sportradar.mobile.player.data.sdk.player.state.controls.PlaybackControlsConfiguration
import ag.sportradar.mobile.player.data.sdk.player.state.controls.createSettingsButtonState
import ag.sportradar.mobile.player.data.sdk.provider.PlaybackAssetProvider
import ag.sportradar.mobile.player.data.sdk.provider.type.ProviderInputType
import ag.sportradar.mobile.player.data.sdk.session.channel.ChannelPlayerState
import ag.sportradar.mobile.player.data.sdk.session.channel.PlaybackChannel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and interactions of the media player.
 *
 * @property assetProvider Provides the playback asset to be played.
 * @property localPlaybackChannel The primary playback channel for media playback, typically implemented with ExoPlayer or AVPlayer.
 * @property additionalChannels Optional list of additional playback channels (e.g., Chromecast).
 */
class PlayerViewModel() : ViewModel(), PlayerControllable, IsolatedKoinComponent {

    /**
     * A singleton instance of [SRAVPlayerConfiguration] that holds global configuration settings for the player.
     *
     * This property provides access to SDK-wide settings, such as license validation status,
     * which can influence the behavior of the ViewModel (e.g., allowing or blocking playback).
     */
    private val configuration: SRAVPlayerConfiguration = SRAVPlayerConfiguration.instance


    private var providerInputType: ProviderInputType? = null

    /**
     * The provider responsible for fetching the [PlaybackAsset] that the player will use.
     * It takes an input of type [ProviderInputType] and returns a [PlaybackAsset] for the [PlaybackChannel] to prepare and play.
     */
    private var assetProvider: PlaybackAssetProvider<ProviderInputType>? = null

    /**
     * The primary, local playback channel for the media player.
     *
     * This channel is responsible for handling playback directly on the device,
     * typically using a native player implementation like ExoPlayer (on Android) or AVPlayer (on iOS).
     * It is considered the default channel for playback.
     */
    private var localPlaybackChannel: PlaybackChannel? = null

    /**
     * A list of secondary playback channels that can be used for casting or other alternative playback scenarios.
     *
     * These channels supplement the [localPlaybackChannel] and allow the user to switch between different playback targets,
     * such as a Chromecast device. If no additional channels are configured, this list will be null or empty.
     */
    private var additionalChannels: List<PlaybackChannel> = emptyList()

    /**
     * Holds the currently active playback channel.
     *
     * This [MutableStateFlow] is updated whenever the active channel changes (e.g., when the user switches between local and additional channels).
     * It should always reference the channel that is currently active for playback and control operations.
     */
    private val _currentActiveChannel: MutableStateFlow<PlaybackChannel?> =
        MutableStateFlow(localPlaybackChannel)

    /**
     * Exposes the currently active playback channel as an immutable [StateFlow].
     *
     * Observers can collect this flow to react to changes in the active channel (e.g., update UI, route commands).
     */
    val currentActiveChannel: StateFlow<PlaybackChannel?> = _currentActiveChannel.asStateFlow()

    /**
     * Holds the current UI state of the player controls.
     */
    private val _controlState: MutableStateFlow<ControlUIState> =
        MutableStateFlow(ControlUIState())

    /**
     * Exposes the control UI state as an immutable flow.
     */
    val controlState: StateFlow<ControlUIState> = _controlState.asStateFlow()

    /**
     * Holds the current error state of the player, if any.
     */
    private val _errorState: MutableStateFlow<SRAVPlayerException?> = MutableStateFlow(null)

    /**
     * Exposes the error state as an immutable flow.
     */
    val errorState: StateFlow<SRAVPlayerException?> = _errorState.asStateFlow()

    /**
     * Indicates whether the player is currently loading.
     */
    private val _isLoadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Exposes the loading state as an immutable flow.
     */
    val isLoadingState: StateFlow<Boolean> = _isLoadingState.asStateFlow()

    /**
     * Duration (in milliseconds) for which the player controls remain visible before auto-hiding.
     */
    private var controlVisibilityMS: Long = 3000L

    /**
     * Reference to the coroutine job responsible for hiding the controls after a delay.
     * Used to cancel any previous scheduled hide operation.
     */
    private var hideControlsJob: Job? = null

    /**
     * Holds the current UI state of the player controls.
     */
    var playbackControlsConfiguration: PlaybackControlsConfiguration =
        PlaybackControlsConfiguration()

    /**
     * Initializes the playback channels and asset provider for the ViewModel.
     *
     * This method sets the [localPlaybackChannel], [additionalChannels], and [assetProvider] properties.
     * It also sets the initial [currentActiveChannel] to the provided [localPlaybackChannel].
     * This function is intended to be called during the setup phase of the player to inject its core dependencies.
     *
     * @param assetProvider The provider responsible for fetching playback assets.
     * @param localPlaybackChannel The primary playback channel (e.g., for local device playback).
     * @param additionalChannels A list of optional secondary playback channels (e.g., for casting).
     */
    fun initializeChannelsValues(
        assetProvider: PlaybackAssetProvider<ProviderInputType>,
        localPlaybackChannel: PlaybackChannel?,
        additionalChannels: List<PlaybackChannel>?
    ) {
        PlayerEventLogger.logInfo("Initializing channels and asset provider for PlayerViewModel")
        this.assetProvider = assetProvider
        this.localPlaybackChannel = localPlaybackChannel
        if (additionalChannels != null) {
            this.additionalChannels = additionalChannels
            PlayerEventLogger.logDebug("Initialized ${additionalChannels.size} additional channels")
        }

        init()
    }

    private fun init() {
        if (configuration.isLicenseValidated == true) {
            PlayerEventLogger.logInfo("License validated, initializing channel state collectors")
            collectChannelTrackState()
            collectChannelProgressState()
            collectPlaybackChannelReadyState()
            collectPlaybackChannelPlayerState()
        } else if (configuration.isLicenseValidated == false) {
            PlayerEventLogger.logError(Throwable("License not validated"), "PlayerViewModel.init")

            _errorState.update {
                LicenceException
            }
        }
    }

    /**
     * Loads a playback asset using the [assetProvider] and prepares the [localPlaybackChannel] for playback.
     * Updates the loading state and resets the ChannelPlayerState if the license is not validated.
     *
     * @param input The input required by the asset provider to fetch the playback asset.
     */
    fun loadAsset(
        input: ProviderInputType,
        uiState: PlaybackControlsConfiguration = PlaybackControlsConfiguration()
    ) {
        viewModelScope.launch {
            reset()
            playbackControlsConfiguration = uiState
            if (configuration.isLicenseValidated == true) {
                PlayerEventLogger.logInfo("Loading asset with input: $input")
                configuration.analyticsProvider?.trackStreamEvent(StreamEvent.LoadingStarted)

                _isLoadingState.value = true

                providerInputType = input

                assetProvider?.provide(input)
                    ?.let { asset ->
                        PlayerEventLogger.logDebug("Asset provided successfully: $asset")
                        localPlaybackChannel?.prepareChannel(asset)
                    }
                    ?: run {
                        PlayerEventLogger.logError(
                            Throwable("Asset Load Error"),
                            "PlayerViewModel.loadAsset"
                        )
                        _errorState.update { AssetException }
                    }
            } else {
                _isLoadingState.value = false
                PlayerEventLogger.logError(
                    Throwable("License not validated"),
                    "PlayerViewModel.loadAsset"
                )
                _errorState.update { LicenceException }
            }
        }
    }

    /**
     * Playback control methods are delegated to the current active [PlaybackChannel].
     */
    override fun playOrPause() {
        PlayerEventLogger.logInfo("Playback started or paused for channel: ${currentActiveChannel.value?.channelId}")
        configuration.analyticsProvider?.trackUserAction(UserAction.Stop)
        currentActiveChannel.value?.playOrPause()
    }

    override fun play() {
        PlayerEventLogger.logInfo("Playback started for channel: ${currentActiveChannel.value?.channelId}")
        configuration.analyticsProvider?.trackUserAction(UserAction.Play)
        currentActiveChannel.value?.play()
    }

    override fun pause() {
        PlayerEventLogger.logInfo("Playback paused for channel: ${currentActiveChannel.value?.channelId}")
        configuration.analyticsProvider?.trackUserAction(UserAction.Pause)
        currentActiveChannel.value?.pause()
    }

    override fun seekTo(positionS: Long) {
        PlayerEventLogger.logDebug("Seeking to position: ${positionS}s")
        configuration.analyticsProvider?.trackUserAction(UserAction.Seek(positionS))
        currentActiveChannel.value?.seekTo(positionS)
        updateUserSeekPosition()
    }

    override fun seekBy(offsetS: Long) {
        PlayerEventLogger.logDebug("Seeking by offset: ${offsetS}s")
        configuration.analyticsProvider?.trackUserAction(UserAction.Seek(offsetS))
        currentActiveChannel.value?.seekBy(offsetS)
        updateUserSeekPosition()
    }

    override fun seekToLive() {
        PlayerEventLogger.logInfo("Seeking to live position")
        configuration.analyticsProvider?.trackUserAction(UserAction.SwitchToLive)
        currentActiveChannel.value?.seekToLive()
        updateUserSeekPosition()
    }

    /**
     * Toggles the fullscreen state of the player controls.
     */
    fun toggleFullscreen() {
        PlayerEventLogger.logDebug("Fullscreen toggled: ${_controlState.value.fullscreenButton.isFullscreen}")
        configuration.analyticsProvider?.trackUserAction(if (_controlState.value.fullscreenButton.isFullscreen) UserAction.ExitFullscreen else UserAction.EnterFullscreen)
        _controlState.update {
            it.copy(
                fullscreenButton = it.fullscreenButton.copy(
                    isFullscreen = !it.fullscreenButton.isFullscreen
                )
            )
        }
    }

    /**
     * Sets the fullscreen state of the player controls.
     *
     * Updates the `isFullscreen` property of the `fullscreenButton` in the control UI state.
     *
     * @param isFullscreen Whether the player should be in fullscreen mode.
     */
    fun setFullscreen(isFullscreen: Boolean) {
        PlayerEventLogger.logDebug("Setting fullscreen to: $isFullscreen")
        _controlState.update {
            it.copy(fullscreenButton = it.fullscreenButton.copy(isFullscreen = isFullscreen))
        }
    }

    /**
     * Sets the active playback channel by its [channelId].
     *
     * Finds the channel with the given [channelId] and updates [_currentActiveChannel] to reference it.
     * If no matching channel is found, defaults to [localPlaybackChannel].
     *
     * Note: This does not update the isActive property of the channels. If you need to reflect the active state in the channel itself,
     * update the isActive property accordingly before or after calling this method.
     *
     * @param channelId The unique identifier of the channel to activate.
     */
    fun toggleActivePlaybackChannel(channelId: String) {
        val allChannels = (listOf(localPlaybackChannel) + additionalChannels)
        var foundActive: PlaybackChannel? = null

        allChannels.forEach { channel ->
            if (channel?.channelId == channelId) foundActive = channel
        }

        if (foundActive != null) {
            PlayerEventLogger.logInfo("Switching to active channel: $channelId")
        } else {
            PlayerEventLogger.logDebug("Channel $channelId not found, defaulting to local channel")
        }

        _currentActiveChannel.value = foundActive ?: localPlaybackChannel
    }

    /**
     * Cleans up all resources used by the PlayerViewModel and its playback channels.
     *
     * This method should be called manually when the PlayerViewModel singleton is no longer needed.
     * It cancels all coroutines launched in the [viewModelScope] to stop ongoing work and subscriptions.
     * Then, it calls [PlaybackChannel.destroyChannel] on all playback channels (local and additional) to ensure they release resources and stop playback.
     *
     * After calling this method, the PlayerViewModel and its channels should not be used again.
     */
    fun destroy() {
        PlayerEventLogger.logInfo("Destroying PlayerViewModel and all channels")
        viewModelScope.cancel()
        val allChannels = (listOf(localPlaybackChannel) + additionalChannels)
        allChannels.forEach {
            it?.destroyChannel()
        }
    }

    /**
     * Resets all playback channels and their states to default values.
     *
     * This method calls [PlaybackChannel.resetChannel] on all playback channels (local and additional),
     * and also resets the UI state and loading state to their initial defaults.
     * The active channel is set to [localPlaybackChannel] by default.
     *
     * After calling this method, the PlayerViewModel and its channels are ready for fresh playback.
     */
    fun reset() {
        PlayerEventLogger.logDebug("Resetting PlayerViewModel state")
        // Reset all playback channels
        val allChannels = (listOf(localPlaybackChannel) + additionalChannels)
        allChannels.forEach { it?.resetChannel() }

        _errorState.update { null }
        _controlState.value = ControlUIState(
            fullscreenButton = _controlState.value.fullscreenButton
        )
        _isLoadingState.value = false
        _currentActiveChannel.update { localPlaybackChannel }
    }

    /**
     * Attempts to reload the playback asset using the last provided input.
     *
     * If [providerInputType] is available, calls [loadAsset] with it.
     * Otherwise, updates the error state to indicate asset load failure.
     */
    fun retry() {
        providerInputType
            ?.let {
                PlayerEventLogger.logInfo("Retrying asset load with previous input")
                loadAsset(it, playbackControlsConfiguration)
            }
            ?: run {
                PlayerEventLogger.logError(
                    Throwable("Retry failed: No previous input"),
                    "PlayerViewModel.retry"
                )
                _errorState.update { AssetException }
            }
    }

    /**
     * Sets the duration (in milliseconds) for which the player controls remain visible.
     *
     * If the provided duration is less than or equal to 3000ms, the value is ignored.
     *
     * @param durationMS The desired visibility duration in milliseconds.
     */
    fun setControlVisibilityDuration(durationMS: Long) {
        if (durationMS <= 3000L) {
            PlayerEventLogger.logDebug("Control visibility duration too short (${durationMS}ms), ignoring")
            return
        }
        PlayerEventLogger.logDebug("Setting control visibility duration to: ${durationMS}ms")
        controlVisibilityMS = durationMS
    }

    /**
     * Locks or unlocks the visibility of the player controls.
     *
     * When locked, the controls will not auto-hide. Cancels any scheduled hide operation
     * and updates the control state to reflect the lock status.
     *
     * @param lock If true, locks the controls' visibility; if false, unlocks it.
     */
    fun lockVisibilityControls(lock: Boolean) {
        _controlState.update {
            cancelScheduleHideControls()
            it.copy(controlVisibility = it.controlVisibility.copy(isLocked = lock))
        }
    }

    /**
     * Shows or hides the player controls based on the [show] parameter.
     *
     * If [show] is false, cancels any scheduled auto-hide job and hides the controls immediately.
     * If [show] is true, makes the controls visible.
     *
     * @param show Whether to show (true) or hide (false) the controls.
     */
    fun toggleControlVisibility(show: Boolean) {
        if (!controlState.value.controlVisibility.isLocked) {
            if (show) {
                _controlState.update {
                    it.copy(
                        controlVisibility = it.controlVisibility.copy(
                            isHidden = false
                        )
                    )
                }
                startScheduledHideControls()
            } else {
                cancelScheduleHideControls()
                _controlState.update {
                    it.copy(
                        controlVisibility = it.controlVisibility.copy(
                            isHidden = true
                        )
                    )
                }
            }
        }
        PlayerEventLogger.logDebug("Controls visibility toggled: $show")
        if (show) {
            _controlState.update { it.copy(controlVisibility = it.controlVisibility.copy(isHidden = false)) }
            startScheduledHideControls()
        } else {
            cancelScheduleHideControls()
            _controlState.update { it.copy(controlVisibility = it.controlVisibility.copy(isHidden = true)) }
        }
    }

    /**
     * Restarts the scheduled job for auto-hiding the player controls.
     *
     * Cancels any previous hide job and starts a new one, ensuring the controls will be hidden
     * after the configured delay.
     */
    fun restartScheduledHideControls() {
        PlayerEventLogger.logDebug("Restarting scheduled hide controls")
        startScheduledHideControls()
    }

    /**
     * Hides the player controls after [controlVisibilityMS] milliseconds.
     *
     * Cancels any previous hide job before starting a new one.
     */
    fun startScheduledHideControls() {
        cancelScheduleHideControls()

        hideControlsJob = viewModelScope.launch {
            delay(controlVisibilityMS)
            if (hideControlsJob?.isActive ?: false) {
                PlayerEventLogger.logDebug("Auto-hiding controls after ${controlVisibilityMS}ms")
                _controlState.update {
                    it.copy(
                        controlVisibility = it.controlVisibility.copy(isHidden = true)
                    )
                }
            }
        }
    }

    /**
     * Cancels any scheduled job for auto-hiding the player controls.
     *
     * Sets the hideControlsJob reference to null after cancellation.
     */
    fun cancelScheduleHideControls() {
        if (hideControlsJob?.isActive == true) {
            PlayerEventLogger.logDebug("Cancelling scheduled hide controls")
        }
        hideControlsJob?.cancel()
        hideControlsJob = null
    }

    /**
     * Sets the user's seek position in the progress scrubber state.
     *
     * Updates the UI state with the provided normalized seek position (0.0 to 1.0).
     * If `percentage` is null, clears the seek position.
     *
     * @param percentage Normalized seek position between 0.0 and 1.0, or null to reset.
     */
    fun updateUserSeekPosition(percentage: Float? = null) {
        _controlState.update {
            it.copy(progressScrubberState = it.progressScrubberState.copy(currentUserSeekPosition = percentage))
        }
    }

    /**
     * Toggles the visibility of the bottom sheet UI component.
     *
     * Updates the [ControlUIState.bottomSheetState] property by setting its value, and setting visible items in the list.
     * This function is used to show or hide a bottom sheet, which typically contains
     * additional options or information related to playback, such as quality settings or subtitles.
     */
    fun toggleBottomSheet(visible: Boolean, items: SettingType? = null) {
        PlayerEventLogger.logDebug("Toggling bottom sheet - visible: $visible, items: ${items?.type?.name}")
        _controlState.update {
            it.copy(
                bottomSheetState = it.bottomSheetState.copy(
                    isVisible = visible,
                    settingsType = items
                )
            )
        }
    }

    /**
     * Selects a specific track for playback on the currently active channel.
     *
     * Delegates the video track selection to the active [PlaybackChannel] by calling its [selectTrack] method
     * with the provided [SRAVTrack] instance. This enables switching between available video qualities, audio or subtitles during playback.
     * Also updates the tracksState and settingsButtonState in the control UI state to reflect the selected track.
     * Deactivates all other tracks of the same type and sets only the selected track as active.
     *
     * @param track The video track to be selected for playback.
     */
    fun selectTrack(track: SRAVTrack) {
        updateSettingsButtonState(track)
        PlayerEventLogger.logDebug("New track selected: $track")
        currentActiveChannel.value?.selectTrack(track)
    }

    /**
     * Subscribes to the isReadyState flow of the currently active playback channel.
     *
     * Uses flatMapLatest to automatically cancel the previous subscription and start collecting from the new channel
     * whenever the active channel changes (via [currentActiveChannel]).
     *
     * When the channel becomes ready, updates the loading and control UI state accordingly.
     */
    private fun collectPlaybackChannelReadyState() {
        viewModelScope.launch {
            currentActiveChannel.flatMapLatest { channel -> channel?.isReadyState ?: emptyFlow() }
                .collect { isReady ->
                    if (isReady) {
                        PlayerEventLogger.logInfo("Playback channel is ready")
                        configuration.analyticsProvider?.trackStreamEvent(
                            StreamEvent.LoadingCompleted(
                                ""
                            )
                        )
                        _isLoadingState.value = false
                    }
                }
        }
    }

    /**
     * Subscribes to the playerState flow of the currently active playback channel.
     *
     * Uses flatMapLatest to automatically cancel the previous subscription and start collecting from the new channel
     * whenever the active channel changes (via [currentActiveChannel]).
     *
     * Updates the control UI state based on the current player state (buffering, ended, error, idle, paused, playing).
     */
    private fun collectPlaybackChannelPlayerState() {
        viewModelScope.launch {
            currentActiveChannel.flatMapLatest { channel -> channel?.playerState ?: emptyFlow() }
                .collect { playerState ->
                    when (playerState) {
                        ChannelPlayerState.Buffering -> {
                            PlayerEventLogger.logDebug("Player state: Buffering")
                            configuration.analyticsProvider?.trackStreamEvent(StreamEvent.BufferingStarted)
                        }

                        ChannelPlayerState.Ended -> {
                            PlayerEventLogger.logInfo("Player state: Ended")
                            _controlState.update {
                                it.copy(playPauseButton = PlayPauseButtonState(state = PlayPauseButtonState.State.Disabled))
                            }
                        }

                        is ChannelPlayerState.ChannelPlayerError -> {
                            PlayerEventLogger.logError(
                                playerState.throwable,
                                "PlayerViewModel.collectPlaybackChannelPlayerState"
                            )
                            configuration.analyticsProvider?.trackError(
                                error = PlaybackError(
                                    message = playerState.throwable.message ?: "Unknown error",
                                    exception = playerState.throwable,
                                    code = "TODO"
                                )
                            )

                            _errorState.update {
                                CustomException(
                                    internalCode = -1,
                                    externalCode = -1,
                                    errorTitle = "Playback Error",
                                    userMessage = "An unknown error occurred during playback.",
                                    errorMessage = playerState.throwable.message ?: "N/A"
                                )
                            }
                        }

                        ChannelPlayerState.Idle -> {
                            PlayerEventLogger.logDebug("Player state: Idle")
                            _controlState.update {
                                it.copy(playPauseButton = PlayPauseButtonState(state = PlayPauseButtonState.State.Disabled))
                            }
                        }

                        ChannelPlayerState.Paused -> {
                            PlayerEventLogger.logDebug("Player state: Paused")
                            configuration.analyticsProvider?.trackStreamEvent(StreamEvent.Pause)
                            _controlState.update {
                                it.copy(playPauseButton = PlayPauseButtonState(state = PlayPauseButtonState.State.Paused))
                            }
                        }

                        ChannelPlayerState.Playing -> {
                            PlayerEventLogger.logDebug("Player state: Playing")
                            configuration.analyticsProvider?.trackStreamEvent(
                                StreamEvent.BufferingCompleted(
                                    0L
                                )
                            )

                            _controlState.update {
                                it.copy(playPauseButton = PlayPauseButtonState(state = PlayPauseButtonState.State.Playing))
                            }
                        }
                    }
                }
        }
    }

    /**
     * Subscribes to the trackState flow of the currently active playback channel.
     *
     * Uses flatMapLatest to automatically switch to the new channel's trackState whenever the active channel changes.
     * Updates the tracksState in the control UI state with the latest track information from the active channel.
     */
    private fun collectChannelTrackState() {
        viewModelScope.launch {
            currentActiveChannel.flatMapLatest { channel -> channel?.trackState ?: emptyFlow() }
                .collect { channelTracks ->
                    PlayerEventLogger.logDebug("Received track state update - Video: ${channelTracks.videoTracks.size}, Audio: ${channelTracks.audioTracks.size}, Subtitle: ${channelTracks.subtitleTracks.size}")

                    val audioTracks = channelTracks.audioTracks.removeDuplications()
                        .filterIsInstance<SRAVTrack.SRAVAudioTrack>()
                    val subtitlesTracks = channelTracks.subtitleTracks.removeDuplications()
                        .filterIsInstance<SRAVTrack.SRAVSubtitleTrack>()
                    val videoTracks = channelTracks.videoTracks
                        .map {
                            it.copy(
                                displayName = computedDisplayName(
                                    width = it.width,
                                    bitrate = it.bitrate,
                                    frameRate = it.frameRate,
                                    codecs = it.codecs,
                                    displayName = it.displayName
                                )
                            )
                        }
                        .removeDuplications()
                        .filterIsInstance<SRAVTrack.SRAVVideoTrack>()

                    val allTracks = listOf(
                        videoTracks,
                        audioTracks,
                        subtitlesTracks
                    ).count { it.isNotEmpty() }

                    val settingsButtonState =
                        if (_controlState.value.settingsButtonState.items.isEmpty() || _controlState.value.settingsButtonState.items.size != allTracks)
                            createSettingsButtonState(
                                videoTracks = videoTracks,
                                audioTracks = audioTracks,
                                subtitleTracks = subtitlesTracks
                            )
                        else null

                    _controlState.update { uiState ->
                        uiState.copy(
                            settingsButtonState = uiState.settingsButtonState.copy(
                                items = settingsButtonState ?: uiState.settingsButtonState.items
                            )
                        )
                    }
                }
        }
    }

    /**
     * Subscribes to the progressState flow of the currently active playback channel.
     *
     * Uses flatMapLatest to automatically switch to the new channel's progressState whenever the active channel changes.
     * Updates the progressScrubberState in the control UI state with the latest progress information from the active channel.
     * Only the progress-related fields are updated; other properties (enabled, isSeekable, isLive) are preserved from the current UI state.
     */
    private fun collectChannelProgressState() {
        viewModelScope.launch {
            currentActiveChannel.flatMapLatest { channel -> channel?.progressState ?: emptyFlow() }
                .collect { channelProgress ->
                    _controlState.update { uiState ->
                        uiState.copy(
                            progressScrubberState = ProgressScrubberState(
                                durationMs = channelProgress.durationMs,
                                currentPositionMs = channelProgress.progressMs,
                                bufferedPosition = channelProgress.bufferProgressMs,
                                currentUserSeekPosition = uiState.progressScrubberState.getCurrentUserSeekPosition(),
                                enabled = uiState.progressScrubberState.enabled,
                                isLive = uiState.progressScrubberState.isLive,
                                isSeekable = uiState.progressScrubberState.isSeekable,
                            )
                        )
                    }
                }
        }
    }

    private fun updateSettingsButtonState(track: SRAVTrack) {
        _controlState.update { uiState ->
            val updatedItems = uiState.settingsButtonState.items.map { settingType ->
                when (track) {
                    is SRAVTrack.SRAVVideoTrack if (settingType.value.firstOrNull() is SRAVTrack.SRAVVideoTrack) -> {
                        settingType.copy(
                            value = settingType.value.map { trackItem ->
                                if (trackItem is SRAVTrack.SRAVVideoTrack) {
                                    if (trackItem.id == track.id) trackItem.copy(isSelected = true) else trackItem.copy(
                                        isSelected = false
                                    )
                                } else trackItem
                            }
                        )
                    }

                    is SRAVTrack.SRAVAudioTrack if (settingType.value.firstOrNull() is SRAVTrack.SRAVAudioTrack) -> {
                        settingType.copy(
                            value = settingType.value.map { trackItem ->
                                if (trackItem is SRAVTrack.SRAVAudioTrack) {
                                    if (trackItem.id == track.id) trackItem.copy(isSelected = true) else trackItem.copy(
                                        isSelected = false
                                    )
                                } else trackItem
                            }
                        )
                    }

                    is SRAVTrack.SRAVSubtitleTrack if (settingType.value.firstOrNull() is SRAVTrack.SRAVSubtitleTrack) -> {
                        settingType.copy(
                            value = settingType.value.map { trackItem ->
                                if (trackItem is SRAVTrack.SRAVSubtitleTrack) {
                                    if (trackItem.id == track.id) trackItem.copy(isSelected = true) else trackItem.copy(
                                        isSelected = false
                                    )
                                } else trackItem
                            }
                        )
                    }

                    else -> settingType
                }
            }

            uiState.copy(
                settingsButtonState = uiState.settingsButtonState.copy(items = updatedItems),
                bottomSheetState = uiState.bottomSheetState.copy(
                    isVisible = false,
                    settingsType = null
                )
            )
        }
    }
}
