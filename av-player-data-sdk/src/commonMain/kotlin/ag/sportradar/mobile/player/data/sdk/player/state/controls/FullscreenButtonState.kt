package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of the fullscreen button in the player controls.
 *
 * @property enabled Indicates if the fullscreen button is enabled and can be interacted with.
 * @property isFullscreen Indicates if the player is currently in fullscreen mode.
 */
data class FullscreenButtonState(
    val enabled: Boolean,
    val isFullscreen: Boolean
)
