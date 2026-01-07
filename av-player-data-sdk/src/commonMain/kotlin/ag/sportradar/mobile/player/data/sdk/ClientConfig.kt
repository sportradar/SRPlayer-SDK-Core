package ag.sportradar.mobile.player.data.sdk

import ag.sportradar.mobile.player.data.sdk.analytics.PlayerAnalyticsProvider
import ag.sportradar.mobile.player.data.sdk.loging.LoggerConfiguration
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import kotlin.native.ObjCName

/**
 * Represents the configuration for the Sportradar SDK client.
 *
 * This class is used to set up and customize the behavior of the SDK client,
 * such as defining access credentials, server endpoints, or other operational parameters.
 * An instance of this configuration is required to initialize the SDK.
 * @param licenseKey The license key from Sportradar
 * @param appKey The app key from CALM portal
 * @param clientId The client ID from CALM portal
 * @param analyticsProvider The analytics provider to be used
 * @param loggingConfig The logging configuration to be used
 */
@ObjCName("AVSRPlayerClientConfig")
data class ClientConfig
@DefaultArgumentInterop.Enabled
constructor(
    val licenseKey: String,
    val appKey: String,
    val clientId: Int,
    val analyticsProvider: PlayerAnalyticsProvider? = null,
    val loggingConfig: LoggerConfiguration = LoggerConfiguration(),
)