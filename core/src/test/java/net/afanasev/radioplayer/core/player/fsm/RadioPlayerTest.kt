package net.afanasev.radioplayer.core.player.fsm

import androidx.media3.common.Player
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RadioPlayerTest {

    private class FakeEffects : PlayerEffects {
        val handled = mutableListOf<PlayerEffect>()
        override fun handle(effect: PlayerEffect) { handled += effect }
        override fun setPlayer(player: Player?) = Unit
    }

    private fun TestScope.player(effects: FakeEffects): RadioPlayer {
        val playerScope = TestScope(StandardTestDispatcher(testScheduler))
        return RadioPlayer(PlayerPolicy(streamUrl = "url"), playerScope.backgroundScope) { effects }
    }

    @Test
    fun `events are reduced in order and their effects run`() = runTest {
        val effects = FakeEffects()
        val player = player(effects)

        player.send(PlayerEvent.MetadataChanged(TrackMetadata("A", null)))
        player.send(PlayerEvent.PlayPauseClicked)
        runCurrent()

        assertEquals(Playback.Connecting, player.state.value.playback)
        assertTrue(PlayerEffect.FetchNextTrack("A") in effects.handled)
        assertTrue(PlayerEffect.StartPlayback("url") in effects.handled)
    }

    @Test
    fun `creating it runs no effects, so the service starts only on play`() = runTest {
        val effects = FakeEffects()
        val player = player(effects)
        runCurrent()

        assertEquals(Playback.Idle, player.state.value.playback)
        assertEquals(emptyList<PlayerEffect>(), effects.handled)
    }
}
