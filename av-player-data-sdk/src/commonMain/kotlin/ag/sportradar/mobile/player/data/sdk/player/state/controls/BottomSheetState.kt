package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the state of a bottom sheet component within the player controls.
 *
 * This data class holds information about the visibility and content of a bottom sheet,
 * which is typically used for displaying options like playback speed, quality, or subtitles.
 *
 * @property isVisible The current visibility state of the bottom sheet.
 * @property settingsType A list of identifiers for the items currently displayed or active within the bottom sheet.
 *                       For example, this could be a list of available video quality levels or subtitle tracks.
 */
data class BottomSheetState(
    val isVisible: Boolean = false,
    val settingsType: SettingType? = null
)