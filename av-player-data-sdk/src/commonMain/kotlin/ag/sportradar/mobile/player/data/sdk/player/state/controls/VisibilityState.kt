package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Holds the state for visibility control.
 *
 * @property isHidden Indicates if the control is hidden.
 * @property isLocked Indicates if the visibility state is locked and cannot be changed.
 */
data class VisibilityState(
    val isHidden: Boolean,
    val isLocked: Boolean
)