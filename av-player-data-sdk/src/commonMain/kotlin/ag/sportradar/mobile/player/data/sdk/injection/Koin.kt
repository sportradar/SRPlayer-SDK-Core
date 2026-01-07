
import ag.sportradar.mobile.player.data.sdk.injection.modules.coroutineDispatcherModule
import ag.sportradar.mobile.player.data.sdk.injection.modules.httpClientModule
import ag.sportradar.mobile.player.data.sdk.injection.modules.playerValidationModule
import ag.sportradar.mobile.player.data.sdk.injection.modules.viewModelModule
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication

/**
 * Provides an isolated Koin context for dependency injection.
 *
 * This avoids polluting or depending on a global Koin instance,
 * which makes it safer for SDKs, testing, and modular usage.
 */
object IsolatedKoinContext {
    // Create a dedicated Koin application with required modules
    private val koinApp = koinApplication {
        modules(coroutineDispatcherModule, httpClientModule, playerValidationModule, viewModelModule) // Register the ViewModel module
    }

    // Expose the Koin instance for retrieval and injection
    val koin: Koin = koinApp.koin
}

/**
 * Base interface for components that want to use the isolated Koin context.
 *
 * Overrides [getKoin] to return the [IsolatedKoinContext]'s Koin instance,
 * instead of the default global Koin instance.
 */
interface IsolatedKoinComponent : KoinComponent {
    override fun getKoin(): Koin = IsolatedKoinContext.koin
}

/**
 * Singleton entry point for accessing the isolated Koin instance.
 *
 * This acts like a shortcut wrapper around [IsolatedKoinComponent].
 */
internal object Koin : IsolatedKoinComponent {

    /**
     * Initializes and returns the isolated Koin instance.
     * Should be called before resolving dependencies.
     *
     * @return the initialized [Koin] context
     */
    fun init(): Koin = getKoin()
}