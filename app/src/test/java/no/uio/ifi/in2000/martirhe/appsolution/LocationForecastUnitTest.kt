package no.uio.ifi.in2000.martirhe.appsolution

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocationForecastUnitTest {

    @Test
    fun testIfLocationForecastReturnsCorrectType() {
        runBlocking {

            val dataSource = LocationForecastDataSource()
            val data = dataSource.fetchLocationForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned data should be of type LocationForecast", data is LocationForecast)
        }
    }

    @Test
    fun testIfLocationForecastGeometryIsNotEmpty() {
        runBlocking {

            val dataSource = LocationForecastDataSource()
            val data = dataSource.fetchLocationForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The list of coordinates should not be empty.", data.geometry.coordinates.isNotEmpty())
        }
    }

    @Test
    fun testIfLocationForecastAirTemperatureIsDouble() {
        runBlocking {

            val dataSource = LocationForecastDataSource()
            val data = dataSource.fetchLocationForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned temperature should be double", data.properties.timeseries[0].data.instant.details.air_temperature is Double)
        }
    }

    @Test
    fun testIfLocationForecastWindDirectionIsDouble() {
        runBlocking {

            val dataSource = LocationForecastDataSource()
            val data = dataSource.fetchLocationForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned temperature should be double", data.properties.timeseries[0].data.instant.details.wind_from_direction is Double)
        }
    }
}