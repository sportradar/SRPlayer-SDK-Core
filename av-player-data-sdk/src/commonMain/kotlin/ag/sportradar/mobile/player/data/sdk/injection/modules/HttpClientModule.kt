package ag.sportradar.mobile.player.data.sdk.injection.modules

import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Provides a singleton instance of Ktor's HttpClient for dependency injection.
 * You may need to configure the engine for multiplatform (Android, iOS, JVM, etc).
 */
val httpClientModule = module {
    single {
        HttpClient() // You can configure the engine if needed
    }
}

