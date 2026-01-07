package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of the play/pause button in the player controls.
 *
 * @property state The current state of the button: Playing, Paused, or Disabled.
 */
data class PlayPauseButtonState(
    val state: State = State.Disabled,
) {
    /**
     * Enum describing possible states of the play/pause button.
     */
    enum class State {
        Playing,
        Paused,
        Disabled
    }
}
