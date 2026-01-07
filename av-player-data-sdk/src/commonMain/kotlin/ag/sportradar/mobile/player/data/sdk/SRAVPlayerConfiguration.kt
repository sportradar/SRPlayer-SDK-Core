package ag.sportradar.mobile.player.data.sdk

import IsolatedKoinComponent
import Koin
import ag.sportradar.mobile.player.data.sdk.SRAVPlayerConfiguration.Companion.instance
import ag.sportradar.mobile.player.data.sdk.analytics.PlayerAnalyticsProvider
import ag.sportradar.mobile.player.data.sdk.injection.DispatcherType
import ag.sportradar.mobile.player.data.sdk.loging.LogLevel
import ag.sportradar.mobile.player.data.sdk.loging.LoggerConfiguration
import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import ag.sportradar.mobile.player.data.sdk.network.calm.SRAVPlayerInitializer
import ag.sportradar.mobile.player.data.sdk.utils.CommonContext
import ag.sportradar.mobile.player.data.sdk.utils.NoAntilog
import ag.sportradar.mobile.player.data.sdk.utils.PlatformInfo
import ag.sportradar.mobile.player.data.sdk.utils.getApplicationId
import ag.sportradar.mobile.player.data.sdk.utils.getBundleId
import ag.sportradar.mobile.player.data.sdk.utils.getCertificateFingerprint
import ag.sportradar.mobile.player.data.sdk.utils.getDefaultLocale
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.qualifier.qualifier
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Singleton configuration class for SRAVPlayer.
 *
 * Holds global settings such as license key, localization, and debug mode.
 * Access the singleton via [instance].
 */
class SRAVPlayerConfiguration private constructor() : IsolatedKoinComponent, SRAVDataSdkInitializer() {


    val dispatcher =
        getKoin().get<CoroutineDispatcher>(qualifier = qualifier(DispatcherType.DEFAULT))

    /**
     * Indicates whether the current SRAVPlayer license is valid.
     * This flag is set to `true` after a successful validation against the CALM service.
     */
    internal var isLicenseValidated: Boolean? = null
        private set

    /**
     * License key required to use SRAVPlayer features.
     *
     * Use [setLicenseKey] to set or update the key.
     */
    internal var licenseKey: String = ""
        private set

    /**
     * iOS: Bundle identifier of the app.
     *
     * Use [setBundleId] to set or update the key.
     * Android: Not used.
     */
    internal var bundleId: String = ""
        private set

    /**
     * Android: Application ID of the app.
     *
     * Use [setApplicationId] to set or update the key.
     * iOS: Not used.
     */
    internal var applicationId: String = ""
        private set

    /**
     * Android: Certificate fingerprint for app verification.
     *
     * Use [setCertificateFingerprint] to set or update the key.
     * iOS: Not used.
     */
    internal var certificateFingerprint: String = ""
        private set

    /**
     * Localization for the player, used to adjust language or regional settings.
     *
     * Defaults to the device locale via [ag.sportradar.mobile.player.data.sdk.utils.getDefaultLocale].
     * Use [setLocalization] to update.
     */
    internal var localization: String = getDefaultLocale()
        private set

    /**
     * A unique key identifying the application.
     *
     * Use [setAppKey] to set or update the key.
     * The application key for authentication with the Sport SDK services.
     * This secret key is provided by Sportradar and should be kept secure.
     * Must not be blank.
     */
    internal var appKey: String = ""
        private set

    /**
     * The unique identifier for the client application.
     *  Use [setAppKey] to set or update the key.
     *
     * This ID is used for authentication and tracking purposes within the Sportradar ecosystem.
     * The unique client identifier assigned to your application by Sportradar.
     * This ID is used to identify your organization and validate your license.
     * Must be a positive number.
     */
    internal var clientId: Int = -1
        private set

    /**
     * The analytics provider instance used for tracking events within the player.
     * This property holds the reference to the analytics service integration.
     */
    internal var analyticsProvider: PlayerAnalyticsProvider? = null
        private set

    /**
     * The logging configuration used by the player.
     *
     * Defaults to a new [LoggerConfiguration] instance.
     * Use [setLoggingProvider] to update the logging configuration.
     */
    internal var loggerConfig: LoggerConfiguration? = LoggerConfiguration()
        private set

    /**
     * Set or update the license key.
     *
     * @param licenseKey The new license key string.
     */
    internal fun setLicenseKey(licenseKey: String) {
        this.licenseKey = licenseKey
    }

    /**
     * Set or update the bundle identifier.
     *
     * Used only on iOS.
     * @param bundleId The new bundle identifier string.
     */
    internal fun setBundleId(bundleId: String) {
        if (!PlatformInfo().isAndroid) {
            this.bundleId = bundleId
        }
    }

    /**
     * Set or update the application ID.
     *
     * Used only on Android.
     * @param applicationId The new application ID string.
     */
    internal fun setApplicationId(applicationId: String) {
        if (PlatformInfo().isAndroid) {
            this.applicationId = applicationId
        }
    }

    /**
     * Set or update the certificate fingerprint for app verification.
     *
     * Used only on Android.
     * @param certificateFingerprint The new certificate fingerprint string.
     */
    internal fun setCertificateFingerprint(certificateFingerprint: String) {
        if (PlatformInfo().isAndroid) {
            this.certificateFingerprint = certificateFingerprint
        }
    }

    /**
     * Set or update the localization string.
     *
     * @param localization The new localization (e.g., "en-US").
     */
    internal fun setLocalization(localization: String) {
        this.localization = localization
    }

    /**
     * Sets the application key for the SRAVPlayer.
     * It is a mandatory parameter for initializing the player.
     *
     * @param appKey The application key provided by Sportradar.
     */
    internal fun setAppKey(appKey: String) {
        this.appKey = appKey
    }

    /**
     * Set or update the client ID for authentication.
     *
     * @param clientId The unique client identifier.
     */
    internal fun setClientId(clientId: Int) {
        this.clientId = clientId
    }

    /**
     * Sets the analytics provider for the player.
     *
     * This function allows the integration of a custom analytics provider implementation,
     * enabling the tracking of player events and user interactions.
     *
     * @param analyticsProvider The analytics provider instance to be used.
     */
    internal fun setAnalyticsProvider(analyticsProvider: PlayerAnalyticsProvider?) {
        this.analyticsProvider = analyticsProvider
    }

    /**
     * Sets the logging provider configuration for the player.
     *
     * This function allows you to specify a custom logging configuration,
     * which controls how logs are handled within the player SDK.
     *
     * @param loggerConfiguration The logging configuration to be used, or null to disable logging.
     */
    internal fun setLoggingProvider(loggerConfiguration: LoggerConfiguration?) {
        this.loggerConfig = loggerConfiguration

        if (loggerConfiguration?.minLogLevel != LogLevel.NONE) {
            Napier.base(DebugAntilog("SRAVPlayer"))
        } else {
            Napier.base(NoAntilog())
        }
    }

    /**
     * Initializes SRAVPlayer with configuration and validates with CALM.
     *
     * @param clientConfig Client configuration for SRAVPlayer.
     */
    override suspend fun initWithContext(
        context: CommonContext,
        clientConfig: ClientConfig
    ): Boolean = suspendCoroutine { continuation ->
        Koin.init()
        instance.run {
            setLicenseKey(clientConfig.licenseKey)
            if (getApplicationId(context).isNotEmpty()) {
                setApplicationId(getApplicationId(context))
                PlayerEventLogger.logInfo("Application ID set to: $applicationId")
            }
            if (getCertificateFingerprint(context).isNotEmpty()) {
                setCertificateFingerprint(
                    getCertificateFingerprint(context)
                )
                PlayerEventLogger.logInfo("Certificate fingerprint set to: $certificateFingerprint")
            }
            if (getBundleId().isNotEmpty()) {
                setBundleId(getBundleId())
                PlayerEventLogger.logInfo("Bundle ID set to: $bundleId")
            }
            setAppKey(clientConfig.appKey)
            setClientId(clientConfig.clientId)
            setAnalyticsProvider(clientConfig.analyticsProvider)
            setLoggingProvider(clientConfig.loggingConfig)
        }

        val appIdentifier = if (PlatformInfo().isAndroid) certificateFingerprint else bundleId

        //TODO when CALM is setup properly, remove this
        val validBundleIds = listOf("ag.sportradar.SRAVPlayerDemo")
        val validFingerprints = listOf("67:CD:BC:64:EE:BF:08:BC:0E:3F:C3:B9:E8:0D:9E:65:8A:01:B8:A9:5F:6A:33:65:6D:16:19:F3:98:BC:04:3D")

        val isValid = if (PlatformInfo().isAndroid) {
            validFingerprints.contains(certificateFingerprint)
        } else {
            validBundleIds.contains(bundleId)
        }

        if (isValid) {
            isLicenseValidated = true
            continuation.resume(true)
        } else {
            isLicenseValidated = false
            continuation.resume(false)
        }

//        CoroutineScope(dispatcher).launch {
//            val initializer = SRAVPlayerInitializer()
//            initializer.validateWithCALM(
//                appIdentifier = appIdentifier,
//                clientId = clientId,
//                appKey = appKey,
//                onFailure = {
//                    isLicenseValidated = false
//                    continuation.resume(false)
//                },
//                onSuccess = {
//                    isLicenseValidated = true
//                    continuation.resume(true)
//                }
//            )
//        }

    }

    companion object {
        /** Singleton instance of [SRAVPlayerConfiguration]. */
        // Todo remove
        val instance: SRAVPlayerConfiguration by lazy {
            SRAVPlayerConfiguration()
        }
    }
}