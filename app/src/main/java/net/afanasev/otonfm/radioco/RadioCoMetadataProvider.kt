package net.afanasev.otonfm.radioco

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json

private const val TIMEOUT_MS = 6_000L
private const val RETRY_MAX_COUNT = 3
private const val RETRY_DELAY_MS = 2_000L

/**
 * Fetches artwork and the next track from the [radio.co](https://radio.co) public status API.
 * Failures are reported through the callbacks and returned as null.
 */
class RadioCoMetadataProvider(
    stationId: String,
    private val onFetchError: (source: String, throwable: Throwable) -> Unit,
    private val onArtworkMismatch: (attempt: Int, maxAttempts: Int, retryDelayMs: Long) -> Unit,
    private val httpClient: HttpClient = defaultHttpClient(),
) {

    private val statusUrl = "https://public.radio.co/stations/$stationId/status?v="
    private val nextTrackUrl = "https://public.radio.co/stations/$stationId/next?v="

    /** Returns the artwork for [expectedTitle], or null if unavailable or mismatched. */
    suspend fun fetchArtworkUri(expectedTitle: String): String? {
        repeat(RETRY_MAX_COUNT) { attemptIdx ->
            loadStatus()?.let {
                if (it.currentTrack.title == expectedTitle) {
                    return it.currentTrack.artworkUri
                }
            }
            onArtworkMismatch(attemptIdx + 1, RETRY_MAX_COUNT, RETRY_DELAY_MS)
            delay(RETRY_DELAY_MS)
        }

        return null
    }

    private suspend fun loadStatus(): StatusModel? {
        return try {
            httpClient.get(statusUrl + System.currentTimeMillis()).body()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            onFetchError("status", e)
            null
        }
    }

    /** Returns the title of the next scheduled track, or null if unknown. */
    suspend fun fetchNextTrack(): String? {
        return try {
            val response: NextTrackModel = httpClient.get(nextTrackUrl + System.currentTimeMillis()).body()
            response.nextTrack.title
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            onFetchError("next_track", e)
            null
        }
    }
}

private fun defaultHttpClient(): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = TIMEOUT_MS
        connectTimeoutMillis = TIMEOUT_MS
        socketTimeoutMillis = TIMEOUT_MS
    }
}
