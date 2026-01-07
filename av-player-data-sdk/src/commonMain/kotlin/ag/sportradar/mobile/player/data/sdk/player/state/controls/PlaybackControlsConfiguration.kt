package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Defines the enabled/disabled state for specific UI components within the player interface.
 *
 * This configuration controls whether individual elements are interactive or visible to the user,
 * allowing for granular customization of the player's UI capabilities.
 *
 * @property controlsLayer Whether the main controls overlay (containing buttons, time, etc.) is enabled.
 * @property progressBar Whether the progress scrubber/timeline is enabled and interactive.
 * @property centerPlayButton Whether the large play/pause button in the center of the screen is enabled.
 * @property titleText Whether the media title display is enabled.
 * @property pictureInPicture Whether the Picture-in-Picture (PiP) mode functionality is enabled.
 * @property settingsMenu Whether the settings menu (for tracks, quality, etc.) is accessible.
 * @property fullscreenToggle Whether the fullscreen toggle button is enabled.
 * @property replayButton Whether the replay button (typically shown after playback ends) is enabled.
 * @property remotePlayback Whether remote playback controls (e.g., Chromecast) are enabled.
 */
data class PlaybackControlsConfiguration(
    val controlsLayer: Boolean = true,
    val progressBar: Boolean = true,
    val centerPlayButton: Boolean = true,
    val titleText: Boolean = true,
    val pictureInPicture: Boolean = true,
    val settingsMenu: Boolean = true,
    val fullscreenToggle: Boolean = true,
    val replayButton: Boolean = true,
    val remotePlayback: Boolean = true,
)