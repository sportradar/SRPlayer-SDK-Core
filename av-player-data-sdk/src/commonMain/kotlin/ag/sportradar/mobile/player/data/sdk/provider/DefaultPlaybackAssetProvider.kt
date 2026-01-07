package ag.sportradar.mobile.player.data.sdk.provider

import ag.sportradar.mobile.player.data.sdk.asset.PlaybackAsset
import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import ag.sportradar.mobile.player.data.sdk.provider.type.DefaultProviderInput
import ag.sportradar.mobile.player.data.sdk.provider.type.ProviderInputType

/**
 * Default implementation of [PlaybackAssetProvider] for providing playback assets.
 *
 * This provider expects an input of type [DefaultProviderInput] and returns a [PlaybackAsset].
 * If the input is not of the expected type, an error is thrown.
 */
class DefaultPlaybackAssetProvider : PlaybackAssetProvider<ProviderInputType> {
    /**
     * Provides a [PlaybackAsset] for the given [ProviderInputType].
     *
     * @param input The input required to create the playback asset. Must be of type [DefaultProviderInput].
     * @return A [DefaultPlaybackAsset] constructed from the input's stream URL.
     * @throws IllegalArgumentException if the input is not of type [DefaultProviderInput].
     */
    override suspend fun provide(input: ProviderInputType): PlaybackAsset {
        PlayerEventLogger.logDebug("Attempting to provide PlaybackAsset with input: $input")

        val defaultInput = input as? DefaultProviderInput
            ?: run {
                PlayerEventLogger.logError(
                    Throwable("Invalid input type: expected DefaultProviderInput, got ${input::class.simpleName}"),
                    "DefaultPlaybackAssetProvider.provide"
                )
                error("Invalid input type")
            }

        val asset = PlaybackAsset(streamUrl = defaultInput.streamUrl)
        PlayerEventLogger.logDebug("PlaybackAsset provided successfully with streamUrl: ${defaultInput.streamUrl}")

        return asset
    }
}
