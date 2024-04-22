package no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.martirhe.appsolution.BuildConfig
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import javax.inject.Inject

class OceanForecastDataSource @Inject constructor() {

    private val apiKey = BuildConfig.UIO_PROXY_API_KEY

    private val client = HttpClient(CIO) {
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", apiKey)
        }

        install(ContentNegotiation) {
            gson()
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ClientRequestException -> {
                        val status = exception.response.status
                        when (status) {
                            HttpStatusCode.UnprocessableEntity -> {
                                throw IllegalArgumentException("Received 422 Unprocessable Entity: Coordinates are outside the valid domain")
                            }
                            else -> throw exception
                        }
                    }
                    else -> throw exception
                }
            }
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw IllegalStateException("Server returned a non-successful status code: ${response.status}")
                }
            }
        }
    }

    suspend fun fetchOceanForecast(
        lat: Double = 59.920244,
        lon: Double = 10.756355,
    ): OceanForecast {
        val apiString = "weatherapi/oceanforecast/2.0/complete"
        val parameterString = "?lat=$lat&lon=$lon"
        Log.i("API call", "url: https://gw-uio.intark.uh-it.no/in2000/$apiString$parameterString")

//        client.config {
//            HttpResponseValidator {
//                handleResponseExceptionWithRequest { exception, _ ->
//                    when (exception) {
//                        is ClientRequestException -> {
//                            val status = exception.response.status
//                            when (status) {
//                                HttpStatusCode.UnprocessableEntity -> {
//                                    throw IllegalArgumentException("Received 422 Unprocessable Entity: Coordinates are outside the valid domain")
//                                }
//                                else -> throw exception
//                            }
//                        }
//                        else -> throw exception
//                    }
//                }
//                validateResponse { response ->
//                    if (!response.status.isSuccess()) {
//                        throw IllegalStateException("Server returned a non-successful status code: ${response.status}")
//                    }
//                }
//            }
//        }

        try {
            val oceanForecast: OceanForecast = client.get(urlString = "https://gw-uio.intark.uh-it.no/in2000/$apiString$parameterString").body()

            if (oceanForecast.properties.timeseries.isEmpty()) {
                throw NoSuchElementException("No Ocean Forecast at the given location. Too far from coast.")
            }

            return oceanForecast
        } catch (e: Exception) {
            Log.e("OceanForecastDataSource", "Failed to fetch ocean forecast", e)
            throw e
        }

//        val oceanForecast: OceanForecast =
//            client.get(urlString = apiString + parameterString).body()
//
//        return oceanForecast
    }
}