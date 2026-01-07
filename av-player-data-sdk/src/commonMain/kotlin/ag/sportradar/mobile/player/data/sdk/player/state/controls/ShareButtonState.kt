package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of the share button in the player controls.
 *
 * @property enabled Indicates if the share button is enabled and can be interacted with.
 * @property url The URL to be shared when the button is pressed.
 */
data class ShareButtonState(
    val enabled: Boolean,
    val url: String
)
