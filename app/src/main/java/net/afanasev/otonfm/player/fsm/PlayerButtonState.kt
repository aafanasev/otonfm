package net.afanasev.otonfm.player.fsm


enum class PlayerButtonState {
    PAUSED,
    LOADING,
    PLAYING,
}

fun Playback.toButtonState(): PlayerButtonState = when (this) {
    Playback.Connecting,
    Playback.Buffering,
    is Playback.Reconnecting -> PlayerButtonState.LOADING

    Playback.Idle,
    is Playback.Error -> PlayerButtonState.PAUSED

    Playback.Playing -> PlayerButtonState.PLAYING
}
