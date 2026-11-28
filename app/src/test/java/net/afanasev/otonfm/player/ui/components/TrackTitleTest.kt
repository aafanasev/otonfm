package net.afanasev.otonfm.player.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class TrackTitleTest {

    @Test
    fun `splits artist and song`() {
        assertEquals(
            TrackTitle(song = "Үрдүккэ көттөрбүн", artist = "Дайаана"),
            TrackTitle.parse("Дайаана - Үрдүккэ көттөрбүн"),
        )
    }

    @Test
    fun `splits at the first separator only`() {
        assertEquals(
            TrackTitle(song = "Born Again - Remix", artist = "LISA"),
            TrackTitle.parse("LISA - Born Again - Remix"),
        )
    }

    @Test
    fun `keeps dashes without spaces inside the song`() {
        assertEquals(
            TrackTitle(song = "Ырыа-тойук ~", artist = "OTON FM"),
            TrackTitle.parse("OTON FM - Ырыа-тойук ~"),
        )
    }

    @Test
    fun `accepts en and em dashes`() {
        assertEquals(TrackTitle(song = "Teardrop", artist = "Massive Attack"), TrackTitle.parse("Massive Attack – Teardrop"))
        assertEquals(TrackTitle(song = "Teardrop", artist = "Massive Attack"), TrackTitle.parse("Massive Attack — Teardrop"))
    }

    @Test
    fun `title without a separator is all song`() {
        assertEquals(TrackTitle(song = "Сахалыы mix"), TrackTitle.parse("Сахалыы mix"))
        assertEquals(TrackTitle(song = "Ырыа-тойук"), TrackTitle.parse("Ырыа-тойук"))
        assertEquals(TrackTitle(song = ""), TrackTitle.parse(""))
    }

    @Test
    fun `missing artist or song keeps the whole title`() {
        assertEquals(TrackTitle(song = "- Teardrop"), TrackTitle.parse(" - Teardrop"))
        assertEquals(TrackTitle(song = "Massive Attack -"), TrackTitle.parse("Massive Attack - "))
    }
}
