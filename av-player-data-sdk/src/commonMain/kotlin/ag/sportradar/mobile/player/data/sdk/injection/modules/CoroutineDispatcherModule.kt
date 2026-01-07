package ag.sportradar.mobile.player.data.sdk.injection.modules

import ag.sportradar.mobile.player.data.sdk.injection.DispatcherType
import ag.sportradar.mobile.player.data.sdk.utils.IONative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

/**
 * Defines Koin dependency injection bindings for coroutine dispatchers.
 *
 * This module registers [coroutine dispatchers] as a singleton, ensuring that the same
 * instance is shared wherever it is injected within the project.
 */
val coroutineDispatcherModule = module {
    // coroutine dispatchers
    factory<CoroutineDispatcher>(qualifier(DispatcherType.MAIN)) { Dispatchers.Main }
    factory<CoroutineDispatcher>(qualifier(DispatcherType.IO)) { Dispatchers.IONative }
    factory<CoroutineDispatcher>(qualifier(DispatcherType.DEFAULT)) { Dispatchers.Default }
    factory<CoroutineDispatcher>(qualifier(DispatcherType.UNCONFINED)) { Dispatchers.Unconfined }
}