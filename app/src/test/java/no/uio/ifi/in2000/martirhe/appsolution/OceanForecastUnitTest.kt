package no.uio.ifi.in2000.martirhe.appsolution

import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OceanForecastUnitTest {

    @Test
    fun testIfOceanForecastReturnsCorrectType() {
        runBlocking {
            val dataSource = OceanForecastDataSource()
            val data = dataSource.fetchOceanForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned data should be of type OceanForecast", data is OceanForecast)
        }
    }

    @Test
    fun testIfOceanForecastGeometryIsNotEmpty() {
        runBlocking {
            val dataSource = OceanForecastDataSource()
            val data = dataSource.fetchOceanForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The list of coordinates should not be empty.", data.geometry.coordinates.isNotEmpty())
        }
    }

    @Test
    fun testIfOceanForecastSeaWaterTemperatureIsDouble() {
        runBlocking {

            val dataSource = OceanForecastDataSource()
            val data = dataSource.fetchOceanForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned temperature should be double", data.properties.timeseries[0].data.instant.details.seaWaterTemperature is Double)
        }
    }

    @Test
    fun testIfOceanForecastWindDirectionIsDouble() {
        runBlocking {

            val dataSource = OceanForecastDataSource()
            val data = dataSource.fetchOceanForecast(lat = 59.920244, lon = 10.756355)
            assertTrue("The returned wind direction should be double", data.properties.timeseries[0].data.instant.details.seaWaterSpeed is Double)
        }
    }
}