package no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast

import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast

interface OceanForecastRepositoryInterface {
    suspend fun getOceanForecast(
        lat: Double = 59.920244,
        lon: Double = 10.756355,
    ): OceanForecast
}

class OceanForecastRepository(
    private val dataSource: OceanForecastDataSource = OceanForecastDataSource()
) : OceanForecastRepositoryInterface {

    override suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast {
        return dataSource.fetchOceanForecast(lat, lon)
    }
}

