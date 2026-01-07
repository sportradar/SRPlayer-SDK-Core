package ag.sportradar.mobile.player.data.sdk.utils

/**
 * Platform-agnostic function to retrieve the device's default locale in BCP-47 format (e.g., "en-US", "fr-FR").
 *
 * Actual implementations should be provided for each supported platform (Android, iOS, JVM, etc.).
 *
 * This is useful for localization, formatting, and adapting content to the user's language and region.
 *
 * @return The current device locale as a BCP-47 string.
 */
expect fun getDefaultLocale(): String
