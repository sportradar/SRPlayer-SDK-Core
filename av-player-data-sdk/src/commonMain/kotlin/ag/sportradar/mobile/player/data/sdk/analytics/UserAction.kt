package ag.sportradar.mobile.player.data.sdk.analytics


/**
 * User-initiated actions in the player.
 */
sealed class UserAction {
    data object Play : UserAction()
    data object Pause : UserAction()
    data object Stop : UserAction()
    data object Resume : UserAction()
    data class Seek(val seconds: Long) : UserAction()
    data object SwitchToLive : UserAction()
    data object EnterFullscreen : UserAction()
    data object ExitFullscreen : UserAction()
}
