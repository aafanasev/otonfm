package net.afanasev.otonfm.player.ui.components

import net.afanasev.otonfm.player.fsm.ErrorReason
import net.afanasev.otonfm.player.fsm.Playback
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerButtonStateTest {

    @Test
    fun `button mapping`() {
        assertEquals(PlayerButtonState.LOADING, Playback.Connecting.toButtonState())
        assertEquals(PlayerButtonState.LOADING, Playback.Buffering.toButtonState())
        assertEquals(PlayerButtonState.LOADING, Playback.Reconnecting(1).toButtonState())
        assertEquals(PlayerButtonState.PAUSED, Playback.Idle.toButtonState())
        assertEquals(PlayerButtonState.PAUSED, Playback.Error(ErrorReason.NETWORK).toButtonState())
        assertEquals(PlayerButtonState.PLAYING, Playback.Playing.toButtonState())
    }
}
