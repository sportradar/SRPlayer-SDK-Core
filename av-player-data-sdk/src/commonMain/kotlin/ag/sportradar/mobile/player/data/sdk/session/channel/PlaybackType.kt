package ag.sportradar.mobile.player.data.sdk.session.channel

data class PlaybackType(val type: String) {
    companion object {
        val LIVE = PlaybackType("LIVE")
        val VOD = PlaybackType("VOD")
        val UNKNOWN = PlaybackType("UNKNOWN")
    }
}