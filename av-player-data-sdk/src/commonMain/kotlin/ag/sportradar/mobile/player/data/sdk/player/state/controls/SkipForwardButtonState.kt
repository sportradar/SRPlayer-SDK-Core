package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of the skip forward button in the player controls.
 *
 * @property enabled Indicates if the skip forward button is enabled and can be interacted with.
 * @property skipDuration The duration in milliseconds to skip forward when the button is pressed.
 */
data class SkipForwardButtonState(
    val enabled: Boolean,
    val skipDuration: Long
)
