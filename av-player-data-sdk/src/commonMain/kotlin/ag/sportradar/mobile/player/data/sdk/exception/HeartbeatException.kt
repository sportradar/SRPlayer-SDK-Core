package ag.sportradar.mobile.player.data.sdk.exception

interface HeartbeatException : SRAVPlayerException

/**
 * Indicates that the heartbeat key could not be loaded.
 * This error typically occurs when the key is missing, inaccessible, or the loading process fails.
 */
object HeartbeatKeyLoadError : HeartbeatException {
    override val internalCode: Int = 500
    override val externalCode: Int = 500
    override val errorTitle: String = "Heartbeat Error"
    override val userMessage: String = "The heartbeat key could not be loaded."
    override val errorMessage: String = "Failed to load heartbeat key."
}

/**
 * Indicates that an error occurred while processing the heartbeat key.
 * This error typically occurs when the heartbeat key responds with an error or is invalid.
 */
object HeartbeatKeyError : HeartbeatException {
    override val internalCode: Int = 501
    override val externalCode: Int = 501
    override val errorTitle: String = "Heartbeat Error"
    override val userMessage: String = "An error occurred while processing the heartbeat key."
    override val errorMessage: String = "Heartbeat key responded with an error."
}