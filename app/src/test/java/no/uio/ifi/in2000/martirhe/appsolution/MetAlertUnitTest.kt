import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertDataSource
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.MetAlertCollection
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MetAlertUnitTest {

    @Test
    fun testIfMetAlertReturnsCorrectType() {
        runBlocking {
            val dataSource = MetAlertDataSource()
            val data = dataSource.fetchMetAlerts()
            TestCase.assertTrue("The returned data should be of type OceanForecast", data is MetAlertCollection)
        }
    }

    @Test
    fun testIfMetAlertGeometryIsNotEmpty() {
        runBlocking {
            val dataSource = MetAlertDataSource()
            val data = dataSource.fetchMetAlerts()
            TestCase.assertTrue("The returned list should not be empty", listOf(data.features[0].geometry.coordinates).isNotEmpty() )
        }
    }

    @Test
    fun testIfOceanForecastIdIsString() {
        runBlocking {
            val dataSource = MetAlertDataSource()
            val data = dataSource.fetchMetAlerts()
            TestCase.assertTrue("The returned data should be of type String", data.features[0].properties.id is String)
        }
    }
}