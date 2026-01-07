package ag.sportradar.mobile.player.data.sdk.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Provides the IO dispatcher for the iOS platform, which is [Dispatchers.IO].
 */
internal actual val Dispatchers.IONative: CoroutineDispatcher
    get() = this.IO