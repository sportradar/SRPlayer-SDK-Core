package ag.sportradar.mobile.player.data.sdk.exception.ott.api

import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException

interface StreamAccessException : SRAVPlayerException

/**
 * Indicates that the stream access API could not be loaded.
 * This error typically occurs when the API is unavailable or fails to initialize.
 */
object StreamAccessApiNotLoaded : StreamAccessException {
    override val internalCode: Int = 200
    override val externalCode: Int = 200
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "The stream access API could not be loaded."
    override val errorMessage: String = "Failed to load stream access API details."
}

/**
 * Indicates that the user is not allowed to view the stream on multiple devices simultaneously.
 * This error typically occurs due to licensing or access restrictions.
 */
object StreamNotAllowedOnMultipleDevices : StreamAccessException {
    override val internalCode: Int = 201
    override val externalCode: Int = 201
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "You are not allowed to view this stream on multiple devices."
    override val errorMessage: String = "Access denied: multiple device streaming is not permitted."
}

/**
 * Indicates that the user is not allowed to view the stream on a mobile phone.
 * This error typically occurs due to licensing or access restrictions.
 */
object StreamNotAllowedOnMobile : StreamAccessException {
    override val internalCode: Int = 202
    override val externalCode: Int = 202
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "You are not allowed to view this stream on a mobile phone."
    override val errorMessage: String = "Access denied: mobile streaming is not permitted."
}

/**
 * Represents a general error that occurred while accessing the stream.
 */
object StreamAccessError : StreamAccessException {
    override val internalCode: Int = 203
    override val externalCode: Int = 203
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "A general stream access error occurred."
    override val errorMessage: String = "General error while accessing the stream."
}

/**
 * Indicates that the requested stream resource could not be found.
 * This error typically occurs when the resource is missing or the identifier is incorrect.
 */
object StreamAccessResourceNotFound : StreamAccessException {
    override val internalCode: Int = 204
    override val externalCode: Int = 204
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "The requested stream resource could not be found."
    override val errorMessage: String = "Resource not found (LCO API 100)."
}

/**
 * Indicates that the requested method is not allowed for the stream.
 * This error typically occurs when an unsupported operation is attempted.
 */
object StreamAccessMethodNotAllowed : StreamAccessException {
    override val internalCode: Int = 205
    override val externalCode: Int = 205
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "The requested method is not allowed for this stream."
    override val errorMessage: String = "Method not allowed (LCO API 101)."
}

/**
 * Indicates that an internal server error occurred while accessing the stream.
 * This error typically occurs due to unexpected failures on the server side.
 */
object StreamAccessApplicationError : StreamAccessException {
    override val internalCode: Int = 206
    override val externalCode: Int = 206
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "An internal server error occurred while accessing the stream."
    override val errorMessage: String = "Internal server error (LCO API 102)."
}

/**
 * Indicates a bad request due to missing end-user IP information.
 * This error typically occurs when required request parameters are not provided.
 */
object StreamAccessBadRequest : StreamAccessException {
    override val internalCode: Int = 207
    override val externalCode: Int = 207
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "Bad request: missing end-user IP information."
    override val errorMessage: String = "Bad request (LCO API 104): end-user IP missing."
}

/**
 * Indicates that the stream is not available for the requested match.
 * This error typically occurs when the stream is restricted or not scheduled for the match.
 */
object StreamAccessStreamNotAvailable : StreamAccessException {
    override val internalCode: Int = 208
    override val externalCode: Int = 208
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "The stream is not available for the requested match."
    override val errorMessage: String = "Stream not available for match (LCO API 104)."
}

/**
 * Indicates that the user's geo location is not authorized for the requested match.
 * This error typically occurs due to geo-blocking restrictions.
 */
object StreamAccessGeoBlock : StreamAccessException {
    override val internalCode: Int = 209
    override val externalCode: Int = 209
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "Your geo location is not authorized for the requested match."
    override val errorMessage: String = "Geo-blocked: unauthorized location (LCO API 105)."
}

/**
 * Indicates that the stream is not available for the requested content.
 * This error typically occurs when the content is missing or not scheduled for streaming.
 */
object StreamAccessStreamNotFound : StreamAccessException {
    override val internalCode: Int = 210
    override val externalCode: Int = 210
    override val errorTitle: String = "Stream Access Error"
    override val userMessage: String = "The stream is not available for the requested content."
    override val errorMessage: String = "Stream not found for content (LCO API 106)."
}
