package ag.sportradar.mobile.player.data.sdk.network.state

/**
 * A generic loading status that defines what state a specific process is in.
 */
sealed interface LoadingStatus {
    /**
     * The initial state right after instantiation, before anything else has happened.
     */
    data object Initial : LoadingStatus

    /**
     * The process has finished and is currently idling.
     */
    data object Idle : LoadingStatus

    /**
     * The process is currently loading or processing some data.
     */
    data object Loading : LoadingStatus

    /**
     * The process is now idling but finished with an error.
     */
    data class Error(val message: String) : LoadingStatus

    /**
     * Returns true if the process is currently loading or about to process some data.
     *
     * @return True if the status is loading or initial.
     */
    fun isLoading(): Boolean = this is Loading || this is Initial
}