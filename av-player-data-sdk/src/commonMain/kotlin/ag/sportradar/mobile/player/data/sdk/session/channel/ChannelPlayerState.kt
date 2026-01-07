package ag.sportradar.mobile.player.data.sdk.session.channel

/**
 * Represents the possible states of a player within a playback channel.
 *
 * This sealed interface is used to model the lifecycle and error conditions of a media player.
 *
 * States:
 * - [Playing]: The player is actively playing media.
 * - [Paused]: The player is paused.
 * - [Buffering]: The player is buffering data.
 * - [Idle]: The player is idle and not playing any media.
 * - [Ended]: The player has finished playback.
 * - [Error]: The player encountered an error, with details in [throwable].
 */
sealed interface ChannelPlayerState {
    object Playing : ChannelPlayerState
    object Paused : ChannelPlayerState
    object Buffering : ChannelPlayerState
    object Idle : ChannelPlayerState
    object Ended : ChannelPlayerState
    /**
     * Represents an error state in the player.
     *
     * @property throwable The exception or error that caused the failure.
     */
    data class ChannelPlayerError(val throwable: Throwable) : ChannelPlayerState
}

