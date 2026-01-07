package ag.sportradar.mobile.player.data.sdk.utils

import java.util.Locale

/**
 * Retrieves the device's default locale in BCP-47 format (e.g., "en-US", "fr-FR") for JVM and Android platforms.
 *
 * This actual implementation uses [Locale.getDefault().toLanguageTag()] to obtain the locale string.
 * Useful for localization, formatting, and adapting content to the user's language and region.
 *
 * @return The current device locale as a BCP-47 string.
 */
actual fun getDefaultLocale(): String = Locale.getDefault().toLanguageTag()