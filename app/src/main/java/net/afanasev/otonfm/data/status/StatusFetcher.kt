package net.afanasev.otonfm.data.status

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

private const val TAG = "StatusFetcher"
private const val STATUS_URL = "https://public.radio.co/stations/s696f24a77/status?v="
private const val TIMEOUT_MS = 6_000L
private const val RETRY_COUNT = 3
private const val RETRY_MS = 1_000L

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
        repeat(RETRY_COUNT) { retry ->
            loadStatus()?.let {
                if (it.currentTrack.title == expectedTitle) {
                    return it.currentTrack.artworkUri
                }
            }
            Log.w(TAG, "Current track and status mismatch (${retry + 1} of $RETRY_COUNT)")
            delay(RETRY_MS)
        }

        Log.w(TAG, "Cannot load appropriate status: $expectedTitle")
        return DEFAULT_ARTWORK_URI
    }

    private suspend fun loadStatus(): StatusModel? {
        return try {
            httpClient.get(STATUS_URL + System.currentTimeMillis()).body()
        } catch (e: Exception) {
            Log.e(TAG, "Cannot load status", e)
            null
        }
    }

}