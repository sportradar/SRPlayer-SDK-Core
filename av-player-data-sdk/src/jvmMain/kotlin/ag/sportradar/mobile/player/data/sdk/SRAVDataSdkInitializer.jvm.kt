package ag.sportradar.mobile.player.data.sdk

import ag.sportradar.mobile.player.data.sdk.utils.CommonContext
import ag.sportradar.mobile.player.data.sdk.utils.Context

/**
 * Abstract class for initializing the SRAV Player Data SDK.
 *
 * Splits the init call into platform specific implementations that additionally takes a [Context] param on Android and just the [ClientConfig] on other platforms.
 */
actual abstract class SRAVDataSdkInitializer {
    /**
     * Initializes the SDK with the given [clientConfig].
     * Sets up logging, dependency injection, and configures domain settings.
     *
     * @param clientConfig The configuration options for the client.
     * @param clientInjectionModules Additional Koin injection modules with client specifics that will be added to DI.
     * @return True if initialization was successful.
     */
    suspend fun initializeSRAVPlayer(
        clientConfig: ClientConfig,
    ): Boolean = initWithContext(Context, clientConfig)

    /**
     * Initializes the SDK with the given [clientConfig].
     * Sets up logging, dependency injection, and configures domain settings.
     *
     * @param context The application context on Android or a dummy object on other platforms.
     * @param clientConfig The configuration options for the client.
     * @param clientInjectionModules Additional Koin injection modules with client specifics that will be added to DI.
     * @return True if initialization was successful.
     */
    internal actual abstract suspend fun initWithContext(
        context: CommonContext,
        clientConfig: ClientConfig
    ): Boolean
}