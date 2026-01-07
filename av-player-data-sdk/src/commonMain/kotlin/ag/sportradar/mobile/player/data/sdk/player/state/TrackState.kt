package ag.sportradar.mobile.player.data.sdk.player.state

import ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack

/**
 * Holds the available and selected media tracks for the player.
 *
 * This state groups the lists of video, audio, and subtitle tracks, typically reflecting the current track options
 * and selection status for a media session. Each list contains the corresponding track type, and the selected track
 * can be determined by the `isSelected` property of each track.
 *
 * @property videoTracks List of available video tracks.
 * @property audioTracks List of available audio tracks.
 * @property subtitleTracks List of available subtitle tracks.
 */
data class TrackState(
    val videoTracks: List<SRAVTrack.SRAVVideoTrack> = emptyList(),
    val audioTracks: List<SRAVTrack.SRAVAudioTrack> = emptyList(),
    val subtitleTracks: List<SRAVTrack.SRAVSubtitleTrack> = emptyList()
)