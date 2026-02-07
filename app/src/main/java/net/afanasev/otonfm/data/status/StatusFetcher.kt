package net.afanasev.otonfm.data.status

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import net.afanasev.otonfm.log.Logger

private const val STATUS_URL = "https://public.radio.co/stations/s696f24a77/status?v="
private const val NEXT_TRACK_URL = "https://public.radio.co/stations/s696f24a77/next?v="
private const val TIMEOUT_MS = 6_000L
private const val RETRY_MAX_COUNT = 3
private const val RETRY_DELAY_MS = 2_000L

class StatusFetcher {

    private val httpClient = HttpClient(Android) {
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

    suspend fun fetchArtworkUri(expectedTitle: String): String {
        repeat(RETRY_MAX_COUNT) { attemptIdx ->
            loadStatus()?.let {
                if (it.currentTrack.title == expectedTitle) {
                    return it.currentTrack.artworkUri
                }
            }
            Logger.logArtworkMismatch(attemptIdx + 1, RETRY_MAX_COUNT, RETRY_DELAY_MS)
            delay(RETRY_DELAY_MS)
        }

        return DEFAULT_ARTWORK_URI
    }

    private suspend fun loadStatus(): StatusModel? {
        return try {
            httpClient.get(STATUS_URL + System.currentTimeMillis()).body()
        } catch (e: Exception) {
            Logger.logStatusException(e)
            null
        }
    }

    suspend fun fetchNextTrack(): String? {
        return try {
            val response: NextTrackModel = httpClient.get(NEXT_TRACK_URL + System.currentTimeMillis()).body()
            response.nextTrack.title
        } catch (e: Exception) {
            Logger.logNextTrackException(e)
            null
        }
    }

}