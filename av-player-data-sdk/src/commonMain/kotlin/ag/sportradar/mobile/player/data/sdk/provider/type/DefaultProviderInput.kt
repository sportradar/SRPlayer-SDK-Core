package ag.sportradar.mobile.player.data.sdk.provider.type

/**
 * Default implementation of [ProviderInputType] for playback providers.
 *
 * @property streamUrl The URL of the media stream to be played.
 */
data class DefaultProviderInput(val streamUrl: String) : ProviderInputType