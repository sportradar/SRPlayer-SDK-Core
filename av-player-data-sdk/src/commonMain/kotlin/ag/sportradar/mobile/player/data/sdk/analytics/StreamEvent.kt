package ag.sportradar.mobile.player.data.sdk.analytics


/**
 * Stream-related events in the player.
 */
sealed class StreamEvent {
    data object LoadingStarted : StreamEvent()
    data class LoadingCompleted(val streamUrl: String) : StreamEvent()
    data object BufferingStarted : StreamEvent()
    data object Pause : StreamEvent()
    data class BufferingCompleted(val durationMs: Long) : StreamEvent()
}
