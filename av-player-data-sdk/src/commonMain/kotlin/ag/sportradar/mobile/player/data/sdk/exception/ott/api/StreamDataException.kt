package ag.sportradar.mobile.player.data.sdk.exception.ott.api

import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException

interface StreamDataException : SRAVPlayerException

/**
 * Indicates that the stream data could not be loaded.
 * This error typically occurs when the data source is unavailable or fails to initialize.
 */
object StreamDataNotLoaded : StreamDataException {
    override val internalCode: Int = 300
    override val externalCode: Int = 300
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "The stream data could not be loaded."
    override val errorMessage: String = "Failed to load stream data details."
}

/**
 * Indicates that access to the stream is blocked due to geographic restrictions.
 * This error typically occurs when the user's location is not authorized to view the stream.
 */
object StreamDataGeoBlock : StreamDataException {
    override val internalCode: Int = 301
    override val externalCode: Int = 301
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "Access to the stream is blocked due to geographic restrictions."
    override val errorMessage: String = "Geoblock applied: ESI -1."
}

/**
 * Indicates that the provided token is not valid for stream access.
 * This error typically occurs when authentication fails due to an invalid token.
 */
object StreamDataTokenNotValid : StreamDataException {
    override val internalCode: Int = 302
    override val externalCode: Int = 302
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "The provided token is not valid for stream access."
    override val errorMessage: String = "Invalid token: ESI -2."
}

/**
 * Indicates that the requested stream is not available.
 * This error typically occurs when the stream is restricted, removed, or not scheduled.
 */
object StreamDataStreamNotAvailable : StreamDataException {
    override val internalCode: Int = 303
    override val externalCode: Int = 303
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "The requested stream is not available."
    override val errorMessage: String = "Stream not available: ESI -3."
}

/**
 * Indicates that the token for stream access has expired.
 * This error typically occurs when the authentication token is no longer valid.
 */
object StreamDataTokenExpired : StreamDataException {
    override val internalCode: Int = 304
    override val externalCode: Int = 304
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "The token for stream access has expired."
    override val errorMessage: String = "Token expired: ESI -4."
}

/**
 * Indicates that the stream has been removed and is no longer available.
 * This error typically occurs when the stream is deleted or taken offline.
 */
object StreamDataStreamRemoved : StreamDataException {
    override val internalCode: Int = 306
    override val externalCode: Int = 306
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "The stream has been removed and is no longer available."
    override val errorMessage: String = "Stream removed: ESI -6."
}

/**
 * Represents a generic error that occurred while accessing stream data.
 */
object StreamDataGenericError : StreamDataException {
    override val internalCode: Int = 307
    override val externalCode: Int = 307
    override val errorTitle: String = "Stream Data Error"
    override val userMessage: String = "A generic error occurred while accessing stream data."
    override val errorMessage: String = "Generic stream data error."
}
