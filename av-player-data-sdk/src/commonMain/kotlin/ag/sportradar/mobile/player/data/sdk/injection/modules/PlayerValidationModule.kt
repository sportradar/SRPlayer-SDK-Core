package ag.sportradar.mobile.player.data.sdk.injection.modules

import ag.sportradar.mobile.player.data.sdk.SRAVPlayerConfiguration
import org.koin.dsl.module

/**
 * Provides a validated singleton instance of SRAVPlayerConfiguration.
 */
val playerValidationModule = module {
    single { SRAVPlayerConfiguration.instance }
}

