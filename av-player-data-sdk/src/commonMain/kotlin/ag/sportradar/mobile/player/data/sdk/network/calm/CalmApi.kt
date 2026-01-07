package ag.sportradar.mobile.player.data.sdk.network.calm

import IsolatedKoinComponent
import ag.sportradar.mobile.player.data.sdk.BuildConfig
import ag.sportradar.mobile.player.data.sdk.injection.DispatcherType
import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import ag.sportradar.mobile.player.data.sdk.network.common.NetworkResponse
import ag.sportradar.mobile.player.data.sdk.network.common.Networking
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.get
import org.koin.core.qualifier.qualifier

/**
 * A singleton object responsible for making network requests to the CALM (Client and Licensing Management) API.
 * This API is used for validating client licenses and retrieving configuration.
 *
 * It uses Koin for dependency injection to get the IO dispatcher for running network operations
 * on a background thread.
 */
internal object CalmApi : IsolatedKoinComponent {
    private val ioDispatcher: CoroutineDispatcher
        get() = get<CoroutineDispatcher>(qualifier(DispatcherType.IO))

    internal suspend fun clientLicenceValidation(clientId: Int, appIdentifier: String, appKey: String): NetworkResponse<CalmClientValidationResponse> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logInfo("Initiating CALM client license validation for clientId: $clientId")
            Networking.get(
                URLBuilder(
                    protocol = URLProtocol.HTTPS,
                    host = BuildConfig.CALM_BASE_URL,
                    pathSegments = listOf("api", "v1", "consumer", "$clientId", "licensing-and-configuration", "validate"),
                    parameters = Parameters.build {
                        append(calmAppIdentifierParameter, appIdentifier)
                        append("appkey", appKey)
                    },
                ).build()
            )
        }
}