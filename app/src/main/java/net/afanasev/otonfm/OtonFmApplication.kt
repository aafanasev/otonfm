package net.afanasev.otonfm

import android.app.Application
import net.afanasev.otonfm.util.log.OtonFmPlayerAnalytics
import net.afanasev.radioplayer.core.RadioPlayerConfig
import net.afanasev.radioplayer.core.RadioPlayerHost
import net.afanasev.radioplayer.core.analytics.PlayerAnalytics
import net.afanasev.radioplayer.core.metadata.NowPlayingMetadataProvider
import net.afanasev.radioplayer.radioco.RadioCoMetadataProvider

private const val RADIO_CO_STATION_ID = "s696f24a77"

class OtonFmApplication : Application(), RadioPlayerHost {

    override val playerAnalytics: PlayerAnalytics = OtonFmPlayerAnalytics

    override val radioPlayerConfig: RadioPlayerConfig by lazy {
        RadioPlayerConfig(
            streamUrl = getString(R.string.stream_url),
            stationName = getString(R.string.app_name),
            defaultArtworkUri = getString(R.string.default_artwork_uri),
            sessionActivityClass = MainActivity::class.java,
        )
    }

    override val nowPlayingMetadataProvider: NowPlayingMetadataProvider by lazy {
        RadioCoMetadataProvider(stationId = RADIO_CO_STATION_ID, analytics = playerAnalytics)
    }
}
