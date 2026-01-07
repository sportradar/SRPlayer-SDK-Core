package ag.sportradar.mobile.player.data.sdk.injection.modules

import ag.sportradar.mobile.player.data.sdk.player.PlayerViewModel
import org.koin.dsl.module

/**
 * Koin module for providing ViewModel dependencies.
 */
val viewModelModule = module {
    factory { PlayerViewModel() }
}
