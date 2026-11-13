package net.afanasev.radioplayer.core.analytics

import net.afanasev.radioplayer.core.player.PlayerButtonState

object NoOpPlayerAnalytics : PlayerAnalytics {
    override fun onPlayButtonClick(state: PlayerButtonState) = Unit
    override fun onMetadataFetchError(source: String, throwable: Throwable) = Unit
    override fun onArtworkMismatch(attempt: Int, maxAttempts: Int, retryDelayMs: Long) = Unit
}
