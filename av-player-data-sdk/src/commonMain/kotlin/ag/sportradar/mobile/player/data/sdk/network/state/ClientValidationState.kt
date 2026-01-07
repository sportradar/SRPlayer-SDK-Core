package ag.sportradar.mobile.player.data.sdk.network.state

/**
 * Represents the state of client validation in the player network layer.
 *
 * @property loadingStatus Current loading status of the validation process.
 * @property isValid Indicates if the client validation was successful.
 */
data class ClientValidationState(
    val loadingStatus: LoadingStatus = LoadingStatus.Initial,
    val isValid: Boolean = false,
)