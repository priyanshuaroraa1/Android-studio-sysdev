package no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.martirhe.appsolution.BuildConfig
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection

class FarevarselDataSource {

    // TODO: Security breach?
    private val apiKey = BuildConfig.UIO_PROXY_API_KEY

    private val client = HttpClient(CIO) {
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", apiKey)
        }

        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchFarevarsler(): FarevarselCollection {
        val farevarselCollection: FarevarselCollection =
            client.get("weatherapi/metalerts/2.0/current.json").body()
        return farevarselCollection
    }
}