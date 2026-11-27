package net.afanasev.otonfm.player.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class TrackTitleTest {

    @Test
    fun `splits artist and song`() {
        assertEquals(
            TrackTitle(song = "Үрдүккэ көттөрбүн", artist = "Дайаана"),
            "Дайаана - Үрдүккэ көттөрбүн".toTrackTitle(),
        )
    }

    @Test
    fun `splits at the first separator only`() {
        assertEquals(
            TrackTitle(song = "Born Again - Remix", artist = "LISA"),
            "LISA - Born Again - Remix".toTrackTitle(),
        )
    }

    @Test
    fun `keeps dashes without spaces inside the song`() {
        assertEquals(
            TrackTitle(song = "Ырыа-тойук ~", artist = "OTON FM"),
            "OTON FM - Ырыа-тойук ~".toTrackTitle(),
        )
    }

    @Test
    fun `accepts en and em dashes`() {
        assertEquals(TrackTitle(song = "Teardrop", artist = "Massive Attack"), "Massive Attack – Teardrop".toTrackTitle())
        assertEquals(TrackTitle(song = "Teardrop", artist = "Massive Attack"), "Massive Attack — Teardrop".toTrackTitle())
    }

    @Test
    fun `title without a separator is all song`() {
        assertEquals(TrackTitle(song = "Сахалыы mix"), "Сахалыы mix".toTrackTitle())
        assertEquals(TrackTitle(song = "Ырыа-тойук"), "Ырыа-тойук".toTrackTitle())
        assertEquals(TrackTitle(song = ""), "".toTrackTitle())
    }

    @Test
    fun `missing artist or song keeps the whole title`() {
        assertEquals(TrackTitle(song = "- Teardrop"), " - Teardrop".toTrackTitle())
        assertEquals(TrackTitle(song = "Massive Attack -"), "Massive Attack - ".toTrackTitle())
    }
}
