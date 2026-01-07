package ag.sportradar.mobile.player.data.sdk

import ag.sportradar.mobile.player.data.sdk.utils.CommonContext
import org.koin.core.module.Module

expect abstract class SRAVDataSdkInitializer() {
    /**
     * Initializes the SDK with the given [clientConfig].
     * Sets up logging, dependency injection, and configures domain settings.
     *
     * @param context The application context on Android or a dummy object on other platforms.
     * @param clientConfig The configuration options for the client.
     * @return True if initialization was successful.
     */
    internal abstract suspend fun initWithContext(
        context: CommonContext,
        clientConfig: ClientConfig,
    ): Boolean
}