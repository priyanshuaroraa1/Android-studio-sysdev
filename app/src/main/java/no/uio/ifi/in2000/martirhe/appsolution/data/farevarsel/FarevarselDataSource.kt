package no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection

// Import packagename.BuildConfig??

class FarevarselDataSource {

    // TODO: Security breach? Why wont BuildConfig work properly
    //private val apiKey = "d51d9a9a-cb2e-4299-9c77-d41f1de3b854"

    private val apiKey = BuildConfig.API_KEY_IFI_PROXY


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
            client.get("weatherapi/metalerts/2.0/test.json").body()
        return farevarselCollection
    }
}