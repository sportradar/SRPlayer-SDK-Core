package ag.sportradar.mobile.player.data.sdk.player.state.controls

/**
 * Represents the media information displayed in the player UI.
 *
 * @property title The title of the media, or null if unavailable.
 * @property subtitle The subtitle or description of the media, or null if unavailable.
 * @property imageUrl The URL of the media's image or thumbnail, or null if unavailable.
 */
data class MediaInfoState(
    val title: String?,
    val subtitle: String?,
    val imageUrl: String?
)
