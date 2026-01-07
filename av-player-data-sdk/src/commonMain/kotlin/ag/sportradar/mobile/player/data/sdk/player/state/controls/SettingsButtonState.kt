package ag.sportradar.mobile.player.data.sdk.player.state.controls

import ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack
import ag.sportradar.mobile.player.data.sdk.player.entities.SettingOptionsType
import ag.sportradar.mobile.player.data.sdk.player.state.SettingOption

/**
 * Represents the state of the settings popup in the player controls.
 */
data class SettingsButtonState(
    val items: List<SettingType> = emptyList()
)

/**
 * Represents the different types of settings that can be displayed in the settings menu.
 */
data class SettingType(
    val name: String,
    val value: List<SettingOption>,
    val type: SettingOptionsType,
)

/**
 * Builds a list of SettingType objects for the settings menu, representing available video, audio, and subtitle tracks.
 *
 * - For video: Adds all video tracks plus an "Auto" option (highest bitrate).
 * - For audio: Adds all unique audio tracks (if more than one).
 * - For subtitles: Adds all unique subtitle tracks plus an "Off" option.
 *
 * @param videoTracks the list of [SRAVTrack.SRAVVideoTrack] to display in the video settings
 * @param audioTracks the list of [SRAVTrack.SRAVAudioTrack] to display in the audio settings
 * @param subtitleTracks the list of [SRAVTrack.SRAVSubtitleTrack] to display in the subtitle settings
 * @return a list of [SettingType] representing the settings menu items
 */
internal fun createSettingsButtonState(
    videoTracks: List<SRAVTrack.SRAVVideoTrack>,
    audioTracks: List<SRAVTrack.SRAVAudioTrack>,
    subtitleTracks: List<SRAVTrack.SRAVSubtitleTrack>
): List<SettingType> {
    //Default tracks are added at the end, to be able to select them by default. Maybe there is a better way to do this.
    val highestBitrateTrack = videoTracks.maxByOrNull { it.bitrate }
    val defaultTrackVideo = SRAVTrack.SRAVVideoTrack(
        id = "-1",
        displayName = "Auto",
        isSelected = true,
        width = highestBitrateTrack?.width ?: 0,
        height = highestBitrateTrack?.height ?: 0,
        bitrate = highestBitrateTrack?.bitrate ?: 0,
        frameRate = highestBitrateTrack?.frameRate ?: 0f,
        codecs = highestBitrateTrack?.codecs ?: "",
        isSupported = true
    )

    val isAnyActive = subtitleTracks.find { it.isSelected }
    val defaultTrackSubtitles = SRAVTrack.SRAVSubtitleTrack(
        id = "-1",
        displayName = "Off",
        isSelected = isAnyActive == null,
        language = "",
        isDefault = false
    )

    val items = mutableListOf<SettingType>()

    if (videoTracks.isNotEmpty()) {
        items.add(
            SettingType(
                name = "Video",
                value = videoTracks + defaultTrackVideo,
                type = SettingOptionsType.VIDEO
            )
        )
    }

    if (audioTracks.isNotEmpty() && audioTracks.size > 1) {
        items.add(
            SettingType(
                name = "Audio",
                value = audioTracks,
                type = SettingOptionsType.AUDIO
            )
        )
    }

    if (subtitleTracks.isNotEmpty()) {
        items.add(
            SettingType(
                name = "Subtitle",
                value = subtitleTracks + defaultTrackSubtitles,
                type = SettingOptionsType.SUBTITLE
            )
        )
    }

    return items.toList()
}
