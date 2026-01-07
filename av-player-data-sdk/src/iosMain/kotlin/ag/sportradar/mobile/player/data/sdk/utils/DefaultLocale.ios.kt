package ag.sportradar.mobile.player.data.sdk.utils

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

/**
 * Retrieves the device's default locale in BCP-47 format (e.g., "en-US", "fr-FR") for iOS platforms.
 *
 * This actual implementation uses [NSLocale.currentLocale.localeIdentifier] and replaces underscores with hyphens
 * to conform to the BCP-47 standard.
 * Useful for localization, formatting, and adapting content to the user's language and region.
 *
 * @return The current device locale as a BCP-47 string.
 */
actual fun getDefaultLocale(): String =
    NSLocale.currentLocale.localeIdentifier.replace("_", "-")