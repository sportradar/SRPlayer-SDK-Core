package ag.sportradar.mobile.player.data.sdk.player.state.controls

import ag.sportradar.mobile.player.data.sdk.player.state.TrackState

/**
 * Represents the complete UI state for all player controls.
 *
 * Serves as a single source of truth for control visibility, media information, and the state of each control element.
 *
 * @property controlVisibility Visibility and lock status of controls.
 * @property mediaInfo Current media details (title, subtitle, image).
 * @property fullscreenButton State of the fullscreen toggle.
 * @property shareButtonState State of the share button.
 * @property autoPlayToggle State of the auto-play toggle.
 * @property skipButton State of the skip forward button.
 * @property skipBackwardButtonState State of the skip backward button.
 * @property playPauseButton State of the play/pause button.
 * @property progressScrubberState State of the progress scrubber (seek positions, live status).
 * @property bottomSheetState State of the bottom sheet UI.
 * @property settingsButtonState State of the settings button.
 */

data class ControlUIState(
    val controlVisibility: VisibilityState = VisibilityState(
        isHidden = true,
        isLocked = false
    ),
    val mediaInfo: MediaInfoState = MediaInfoState(title = null, subtitle = null, imageUrl = null),
    val fullscreenButton: FullscreenButtonState = FullscreenButtonState(
        enabled = false,
        isFullscreen = false
    ),
    val shareButtonState: ShareButtonState = ShareButtonState(
        enabled = false,
        url = ""
    ),
    val autoPlayToggle: AutoPlayToggleState = AutoPlayToggleState(
        enabled = false,
        isAutoPlay = false
    ),
    val skipButton: SkipForwardButtonState = SkipForwardButtonState(
        enabled = false,
        skipDuration = 15000L
    ),
    val skipBackwardButtonState: SkipBackwardButtonState = SkipBackwardButtonState(
        enabled = false,
        skipDuration = 15000L
    ),
    val playPauseButton: PlayPauseButtonState = PlayPauseButtonState(PlayPauseButtonState.State.Disabled),
    val progressScrubberState: ProgressScrubberState = ProgressScrubberState(
        durationMs = 0L,
        currentPositionMs = 0L,
        bufferedPosition = 0L,
        isLive = false,
        enabled = false,
        isSeekable = false
    ),
    val bottomSheetState: BottomSheetState = BottomSheetState(),
    val settingsButtonState: SettingsButtonState = SettingsButtonState()
)


