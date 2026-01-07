package ag.sportradar.mobile.player.data.sdk.analytics


/**
 * Interface for integrating custom analytics providers with the AV player.
 * Implement this interface to track player operations and events.
 */
interface PlayerAnalyticsProvider {

    /**
     * Called when a user action occurs in the player.
     * @param action The user action that was performed
     * @param metadata Optional metadata associated with the action
     */
    fun trackUserAction(action: UserAction, metadata: Map<String, Any> = emptyMap())

    /**
     * Called when a stream event occurs.
     * @param event The stream event that occurred
     * @param metadata Optional metadata associated with the event
     */
    fun trackStreamEvent(event: StreamEvent, metadata: Map<String, Any> = emptyMap())

    /**
     * Called when a playback error occurs.
     * @param error The error that occurred
     * @param metadata Optional metadata associated with the error
     */
    fun trackError(error: PlaybackError, metadata: Map<String, Any> = emptyMap())
}