package ag.sportradar.mobile.player.data.sdk.player.entities

import ag.sportradar.mobile.player.data.sdk.player.state.SettingOption

/**
 * Represents a media track in the player, such as audio, subtitle, or video.
 *
 * This sealed class hierarchy allows type-safe handling of different track types with their specific properties.
 *
 * @property id Unique identifier for the track.
 * @property displayName User-friendly name for the track.
 * @property isSelected Indicates if this track is currently selected for playback.
 *
 * Subclasses:
 * - [SRAVAudioTrack]: Represents an audio track with language and default status.
 * - [SRAVSubtitleTrack]: Represents a subtitle track with language and default status.
 * - [SRAVVideoTrack]: Represents a video track with codec, bitrate, frame rate, and support status.
 */
sealed class SRAVTrack(
    override val id: String,
    override val displayName: String,
    override val isSelected: Boolean
) : SettingOption {
    data class SRAVAudioTrack(
        val language: String,
        val isDefault: Boolean,
        override val id: String,
        override val displayName: String,
        override val isSelected: Boolean
    ) : SRAVTrack(
        id = id,
        displayName = displayName,
        isSelected = isSelected
    )

    data class SRAVSubtitleTrack(
        val language: String,
        val isDefault: Boolean,
        override val id: String,
        override val displayName: String,
        override val isSelected: Boolean,
    ) : SRAVTrack(
        id = id,
        displayName = displayName,
        isSelected = isSelected
    )

    data class SRAVVideoTrack(
        val width: Int,
        val height: Int,
        val bitrate: Int,
        val frameRate: Float,
        val codecs: String,
        val isSupported: Boolean,
        override val id: String,
        override val displayName: String,
        override val isSelected: Boolean
    ) : SRAVTrack(
        id = id,
        displayName = displayName,
        isSelected = isSelected
    )
}

fun List<SRAVTrack>.removeDuplications(): List<SRAVTrack> =
    if (!containsDuplicateNames()) this
    else filterIsInstance<SRAVTrack.SRAVVideoTrack>()
        .takeIf { it.isNotEmpty() }
        ?.groupBy { it.displayName }
        ?.map { (_, tracks) -> tracks.maxByOrNull { it.bitrate } ?: tracks.first() }
        ?: distinctBy { it.displayName }

internal fun computedDisplayName(
    width: Int,
    bitrate: Int,
    frameRate: Float,
    codecs: String,
    displayName: String
): String =
    if (displayName.isNotBlank()) displayName
    else if (width > 1) "${width}p"
    else if (bitrate > 0) "${bitrate / 1000}kbps"
    else if (frameRate > 0f) "${frameRate}fps"
    else codecs.ifBlank { "Unknown" }

private fun List<SRAVTrack>.containsDuplicateNames(): Boolean = groupingBy { it.displayName }
    .eachCount().values
    .any { it > 1 }



