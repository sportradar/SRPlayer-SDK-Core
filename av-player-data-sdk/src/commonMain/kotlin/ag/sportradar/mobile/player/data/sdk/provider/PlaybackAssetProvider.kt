package ag.sportradar.mobile.player.data.sdk.provider

import ag.sportradar.mobile.player.data.sdk.asset.PlaybackAsset
import ag.sportradar.mobile.player.data.sdk.provider.type.ProviderInputType

/**
 * Functional interface for asynchronously providing a [ag.sportradar.mobile.player.data.sdk.asset.DefaultPlaybackAsset].
 *
 * Implement this interface to supply media assets to the player on demand.
 * The provider can fetch assets from network, local storage, or any other source.
 */
interface PlaybackAssetProvider<T : ProviderInputType> {

    /**
     * Returns a [PlaybackAsset].
     *
     * This function is suspendable to allow asynchronous operations.
     *
     * @return The [PlaybackAsset] corresponding to the requested URL.
     */
    suspend fun provide(input: T): PlaybackAsset
}