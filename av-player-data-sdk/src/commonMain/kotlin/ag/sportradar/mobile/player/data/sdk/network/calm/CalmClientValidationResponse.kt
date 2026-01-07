package ag.sportradar.mobile.player.data.sdk.network.calm

import kotlinx.serialization.Serializable

/**
 * Represents the response for Calm client validation.
 *
 * @property configs List of client configuration objects.
 */
@Serializable
internal data class CalmClientValidationResponse(
    val configs: List<CalmClientConfig>
)