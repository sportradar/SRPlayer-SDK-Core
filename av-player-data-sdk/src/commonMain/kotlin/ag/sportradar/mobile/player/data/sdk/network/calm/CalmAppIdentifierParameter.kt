package ag.sportradar.mobile.player.data.sdk.network.calm

/**
 * Platform-specific Calm app identifier parameter.
 *
 * This expected value should be implemented in each platform module to provide
 * the appropriate Calm app identifier string for network requests.
 */
internal expect val calmAppIdentifierParameter: String