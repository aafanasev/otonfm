package net.afanasev.otonfm.core

import net.afanasev.otonfm.core.analytics.PlayerAnalytics
import net.afanasev.otonfm.core.metadata.NowPlayingMetadataProvider

/**
 * Implemented by the host [android.app.Application] to supply :core's extension points.
 * [PlaybackService] and [net.afanasev.otonfm.core.player.PlayerViewModel] read this off
 * `application` at runtime, since Android constructs services/view-models without a way to
 * inject constructor arguments directly.
 */
interface RadioPlayerHost {
    val radioPlayerConfig: RadioPlayerConfig
    val nowPlayingMetadataProvider: NowPlayingMetadataProvider
    val playerAnalytics: PlayerAnalytics
}
