package ag.sportradar.mobile.player.data.sdk

import ag.sportradar.mobile.player.data.sdk.BuildConfig.CALM_BASE_URL
import ag.sportradar.mobile.player.data.sdk.BuildConfig.USER_AGENT

/**
 * Holds build configuration constants for the player SDK.
 *
 * @property USER_AGENT The user agent string used for network requests.
 * @property CALM_BASE_URL The base URL for CALM API requests.
 */
internal object BuildConfig {
    const val USER_AGENT: String = "ag.sportradar.mobile.SportSDK / 0.1.196-DEV 197"

    const val CALM_BASE_URL: String = "cfd.staging.calm-nonprod.sportradar.dev"
}
