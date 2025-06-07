package net.afanasev.otonfm.data.status

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val STATUS_URL = "https://public.radio.co/stations/s696f24a77/status?v="
private const val TIMEOUT_MS = 6_000L

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

    suspend fun fetchArtworkUri(): String {
        val status: StatusModel? = try {
            httpClient.get(STATUS_URL + System.currentTimeMillis()).body()
        } catch (_: Exception) {
            null
        }
        return status?.currentTrack?.artworkUri ?: DEFAULT_ARTWORK_URI
    }

}