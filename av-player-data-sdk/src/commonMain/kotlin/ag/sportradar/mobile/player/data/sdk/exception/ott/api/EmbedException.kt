package ag.sportradar.mobile.player.data.sdk.exception.ott.api

import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException

interface EmbedException : SRAVPlayerException

/**
 * Indicates that either the Stream ID or Partner ID (or both) are missing in the configuration.
 * This error typically occurs when required identifiers are not provided for embedding the player.
 */
object EmbedVideoOrPartnerIdMissing : EmbedException {
    override val internalCode: Int = 1
    override val externalCode: Int = 1
    override val errorTitle: String = "Embed Error"
    override val userMessage: String = "Stream ID or Partner ID is missing in the configuration."
    override val errorMessage: String = "Either videoID or partnerID (or both) are missing in the config."
}

/**
 * Indicates that the DOM object required for the player instance is not available.
 * This error typically occurs when the player cannot be instantiated due to missing DOM elements.
 */
object EmbedDomObjectNotAvailable : EmbedException {
    override val internalCode: Int = 2
    override val externalCode: Int = 2
    override val errorTitle: String = "Embed Error"
    override val userMessage: String = "Player object is not available."
    override val errorMessage: String = "DOM object required for player instance is not available."
}