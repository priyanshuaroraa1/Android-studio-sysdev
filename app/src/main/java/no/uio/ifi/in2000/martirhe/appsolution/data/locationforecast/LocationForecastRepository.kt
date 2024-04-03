package no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast

import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import javax.inject.Inject


interface LocationForecastRepositoryInterface {
    suspend fun getLocationForecast(
        lat: Double = 59.920244,
        lon: Double = 10.756355,
    ): LocationForecast
}

class LocationForecastRepository @Inject constructor(
//    private val dataSource: LocationForecastDataSource = LocationForecastDataSource()
    private val dataSource: LocationForecastDataSource
) : LocationForecastRepositoryInterface {

    override suspend fun getLocationForecast(lat: Double, lon: Double): LocationForecast {
        return dataSource.fetchLocationForecast(lat, lon)
    }
}