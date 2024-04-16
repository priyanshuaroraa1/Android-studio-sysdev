package no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast

import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import javax.inject.Inject

interface OceanForecastRepositoryInterface {
    suspend fun getOceanForecast(
        lat: Double,
        lon: Double,
    ): OceanForecast
}

class OceanForecastRepository @Inject constructor(
    private val dataSource: OceanForecastDataSource
) : OceanForecastRepositoryInterface {

    override suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast {
        return dataSource.fetchOceanForecast(lat, lon)
    }
}

