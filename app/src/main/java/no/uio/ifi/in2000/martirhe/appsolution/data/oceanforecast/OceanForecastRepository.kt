package no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast

import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import javax.inject.Inject

interface OceanForecastRepositoryInterface {
    suspend fun getOceanForecast(
        lat: Double = 59.920244,
        lon: Double = 10.756355,
    ): OceanForecast
}

class OceanForecastRepository @Inject constructor(
//    private val dataSource: OceanForecastDataSource = OceanForecastDataSource()
    private val dataSource: OceanForecastDataSource
) : OceanForecastRepositoryInterface {

    override suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast {
        return dataSource.fetchOceanForecast(lat, lon)
    }
}

