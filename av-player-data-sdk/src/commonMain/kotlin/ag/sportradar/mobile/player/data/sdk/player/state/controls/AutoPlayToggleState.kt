package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Holds the state for the auto-play toggle control.
 *
 * @property enabled Indicates if the toggle is enabled and can be interacted with.
 * @property isAutoPlay Indicates if auto-play is currently active.
 */
data class AutoPlayToggleState(
    val enabled: Boolean,
    val isAutoPlay: Boolean
)