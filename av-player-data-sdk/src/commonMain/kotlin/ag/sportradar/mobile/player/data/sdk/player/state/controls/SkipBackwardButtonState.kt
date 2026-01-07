package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of the skip backward button in the player controls.
 *
 * @property enabled Indicates if the skip backward button is enabled and can be interacted with.
 * @property skipDuration The duration in milliseconds to skip backward when the button is pressed.
 */
data class SkipBackwardButtonState(
    val enabled: Boolean,
    val skipDuration: Long,
)
