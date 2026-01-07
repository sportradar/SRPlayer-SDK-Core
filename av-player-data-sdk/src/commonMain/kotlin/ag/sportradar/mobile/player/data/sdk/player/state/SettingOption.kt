package ag.sportradar.mobile.player.data.sdk.player.state

/**
 * Represents a configurable option within the player settings.
 *
 * This interface defines the structure for individual setting choices that can be presented to the user,
 * such as audio tracks, subtitle tracks, or video quality levels.
 *
 * @property id The unique identifier for the option. This is typically used internally to select the specific track or quality level.
 * @property displayName The human-readable label for the option, suitable for UI display (e.g., "English", "1080p").
 * @property isSelected Indicates whether this specific option is currently selected or active.
 */
interface SettingOption {
    val id: String
    val displayName: String
    val isSelected: Boolean
}
