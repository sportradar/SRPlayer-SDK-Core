package ag.sportradar.mobile.player.data.sdk.player.entities

/**
 * Represents the specific type of configuration or setting option available within the player SDK.
 *
 * This enum is used to categorize different player settings, such as video quality, audio tracks,
 * captions, or playback speed, allowing the application to distinguish between the various
 * control interfaces presented to the user.
 */
enum class SettingOptionsType {
    VIDEO,
    AUDIO,
    SUBTITLE
}