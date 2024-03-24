package no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.martirhe.appsolution.BuildConfig
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast

class LocationForecastDataSource {

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

    suspend fun fetchLocationForecast(
        lat: Double = 59.920244,
        lon: Double = 10.756355,
    ): LocationForecast {
        val apiString = "weatherapi/locationforecast/2.0/compact.json"
        val parameterString = "?lat=$lat&lon=$lon"
        Log.i("API call", "url: https://gw-uio.intark.uh-it.no/in2000/$apiString$parameterString")
        val locationForecast: LocationForecast =
            client.get(urlString = apiString + parameterString).body()

        Log.i("API call", "Current temperature ${locationForecast.properties.timeseries[0].data.instant.details.air_temperature}")
        return locationForecast
    }

}