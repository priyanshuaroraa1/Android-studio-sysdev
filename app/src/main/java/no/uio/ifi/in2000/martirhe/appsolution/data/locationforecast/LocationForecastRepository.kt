package no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast

import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import javax.inject.Inject


interface LocationForecastRepositoryInterface {
    suspend fun getLocationForecast(
        lat: Double,
        lon: Double,
    ): LocationForecast
}

class LocationForecastRepository @Inject constructor(
    private val dataSource: LocationForecastDataSource
) : LocationForecastRepositoryInterface {

    override suspend fun getLocationForecast(lat: Double, lon: Double): LocationForecast {
        return dataSource.fetchLocationForecast(lat, lon)
    }
}