package ag.sportradar.mobile.player.data.sdk.network.calm

import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger


/**
 * Initializes and validates the AV player with CALM licensing.
 *
 * The `validateWithCALM` function checks the client licence using the Calm API.
 * It triggers `onSuccess` if the licence is valid, otherwise calls `onFailure`.
 *
 * @constructor Creates an instance of SRAVPlayerInitializer.
 */
class SRAVPlayerInitializer {
    /**
     * Validates the client licence with CALM.
     *
     * @param appIdentifier The application identifier.
     * @param clientId The client ID.
     * @param appKey The application key.
     * @param onSuccess Callback invoked when validation succeeds.
     * @param onFailure Callback invoked when validation fails.
     */
    suspend fun validateWithCALM(
        appIdentifier: String,
        clientId: Int,
        appKey: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        PlayerEventLogger.logInfo("Starting SRAV player validation with CALM for clientId: $clientId, appIdentifier: $appIdentifier")

        CalmApi.clientLicenceValidation(clientId, appIdentifier, appKey).unwrap(
            onSuccess = { response ->
                val isValid = response.configs.firstOrNull()?.valid ?: false
                if (!isValid) {
                    PlayerEventLogger.logWarning("CALM validation returned invalid license status for clientId: $clientId")
                    onFailure()
                } else {
                    PlayerEventLogger.logInfo("CALM validation successful for clientId: $clientId")
                    onSuccess()
                }
            },
            onFailure = { httpError ->
                PlayerEventLogger.logError(Throwable("CALM validation failed for clientId: $clientId - ${httpError.message ?: "Unknown error"}"))
                onFailure()
            }
        )
    }
}
