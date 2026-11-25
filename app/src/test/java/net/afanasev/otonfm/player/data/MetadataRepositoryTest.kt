package net.afanasev.otonfm.player.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

private const val STATION_ID = "sTest123"

private class Reports {
    val metadataErrors = mutableListOf<Pair<String, Throwable>>()
    val artworkMismatches = mutableListOf<Int>()
}

private fun clientOf(engine: MockEngine): HttpClient = HttpClient(engine) {
    install(ContentNegotiation) { json() }
}

private fun repositoryOf(engine: MockEngine, reports: Reports = Reports()) = MetadataRepository(
    stationId = STATION_ID,
    stationName = "Test FM",
    onFetchError = { source, throwable -> reports.metadataErrors += source to throwable },
    onArtworkMismatch = { attempt, _, _ -> reports.artworkMismatches += attempt },
    httpClient = clientOf(engine),
)

private fun jsonHeaders() = headersOf(HttpHeaders.ContentType, "application/json")

class MetadataRepositoryTest {

    @Test
    fun `fetchArtworkUri returns artwork when title matches`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(true, request.url.encodedPath.contains(STATION_ID))
            respond(
                content = """{"current_track":{"title":"Now Playing","artwork_url_large":"https://example.com/art.jpg"}}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders(),
            )
        }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val artwork = repository.fetchArtworkUri("Now Playing")

        assertEquals("https://example.com/art.jpg", artwork)
        assertEquals(emptyList<Int>(), reports.artworkMismatches)
    }

    @Test
    fun `fetchArtworkUri retries and gives up on persistent title mismatch`() = runTest {
        val engine = MockEngine {
            respond(
                content = """{"current_track":{"title":"Different Track","artwork_url_large":"https://example.com/art.jpg"}}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders(),
            )
        }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val artwork = repository.fetchArtworkUri("Expected Track")

        assertNull(artwork)
        assertEquals(listOf(1, 2, 3), reports.artworkMismatches)
    }

    @Test
    fun `fetchArtworkUri reports and swallows HTTP errors`() = runTest {
        val engine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val artwork = repository.fetchArtworkUri("Any Title")

        assertNull(artwork)
        assertEquals(3, reports.metadataErrors.count { it.first == "status" })
    }

    @Test
    fun `fetchNextTrack returns next track title`() = runTest {
        val engine = MockEngine {
            respond(
                content = """{"next_track":{"title":"Upcoming Song"}}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders(),
            )
        }
        val repository = repositoryOf(engine)

        assertEquals("Upcoming Song", repository.fetchNextTrack())
    }

    @Test
    fun `fetchNextTrack reports and swallows errors`() = runTest {
        val engine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val nextTrack = repository.fetchNextTrack()

        assertNull(nextTrack)
        assertEquals(1, reports.metadataErrors.count { it.first == "next_track" })
    }

    @Test
    fun `cancelled fetchArtworkUri reports nothing`() = runTest {
        val started = CompletableDeferred<Unit>()
        val engine = MockEngine {
            started.complete(Unit)
            awaitCancellation()
        }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val job = launch { repository.fetchArtworkUri("Any Title") }
        started.await()
        job.cancelAndJoin()

        assertEquals(emptyList<Pair<String, Throwable>>(), reports.metadataErrors)
        assertEquals(emptyList<Int>(), reports.artworkMismatches)
    }

    @Test
    fun `cancelled fetchNextTrack reports nothing`() = runTest {
        val started = CompletableDeferred<Unit>()
        val engine = MockEngine {
            started.complete(Unit)
            awaitCancellation()
        }
        val reports = Reports()
        val repository = repositoryOf(engine, reports)

        val job = launch { repository.fetchNextTrack() }
        started.await()
        job.cancelAndJoin()

        assertEquals(emptyList<Pair<String, Throwable>>(), reports.metadataErrors)
    }
}
