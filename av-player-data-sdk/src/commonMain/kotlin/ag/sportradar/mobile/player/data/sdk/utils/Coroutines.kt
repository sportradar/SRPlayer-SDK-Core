package ag.sportradar.mobile.player.data.sdk.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides the IO dispatcher for the current platform.
 * This is used instead of Dispatchers.IO because the JS target does not have that dispatcher.
 */
internal expect val Dispatchers.IONative: CoroutineDispatcher
