package ag.sportradar.mobile.player.data.sdk.network.common.errors

import kotlinx.serialization.Serializable

/**
 * Represents an HTTP error response.
 *
 * @property status The HTTP status code of the error (e.g., 400).
 * @property error A short description of the error (e.g., "Bad Request").
 * @property message A more detailed explanation of the error, if available.
 */
@Serializable
internal data class HttpError(
    val status: Int? = null, // 400
    val error: String? = null, // "Bad Request"
    val message: String? = null, // reason why error happened
)