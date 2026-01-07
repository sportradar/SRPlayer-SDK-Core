package ag.sportradar.mobile.player.data.sdk.network.calm

import kotlinx.serialization.Serializable

/**
 * Represents the configuration for a Calm client.
 *
 * @property configId Unique identifier for the configuration.
 * @property configValue The value of the configuration.
 * @property createdAt Timestamp when the configuration was created.
 * @property id Unique identifier for the record.
 * @property organizationVersionId Identifier for the organization version.
 * @property updatedAt Timestamp when the configuration was last updated.
 * @property valid Indicates if the configuration is valid.
 */
@Serializable
internal data class CalmClientConfig(
    val configId: Int,
    val configValue: String,
    val createdAt: String,
    val id: Int,
    val organizationVersionId: Int,
    val updatedAt: String,
    val valid: Boolean
)