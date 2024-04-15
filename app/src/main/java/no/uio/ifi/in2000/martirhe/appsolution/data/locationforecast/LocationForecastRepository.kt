package no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import javax.inject.Inject


interface LocationForecastRepositoryInterface {
    suspend fun getLocationForecast(lat: Double, lon: Double): LocationForecast
    suspend fun getForecastNextHour(lat: Double, lon: Double): ForecastNextHour
}

class LocationForecastRepository @Inject constructor(
    private val dataSource: LocationForecastDataSource
) : LocationForecastRepositoryInterface {

    override suspend fun getLocationForecast(lat: Double, lon: Double): LocationForecast {
        return dataSource.fetchLocationForecast(lat, lon)
    }

    override suspend fun getForecastNextHour(lat: Double, lon: Double): ForecastNextHour {
        val locationForecast = getLocationForecast(lat, lon)

        val symbolCode = locationForecast.properties.timeseries[0].data.next_1_hours?.summary?.symbol_code
        Log.i("Next hour forecast", symbolCode.toString())
        val airTemperature = locationForecast.properties.timeseries[0].data.instant.details.air_temperature
        val precipitationAmount = locationForecast.properties.timeseries[0].data.next_1_hours?.details?.precipitation_amount
        Log.i("Next hour forecast", precipitationAmount.toString() + " mm")
        val windFromDirection = locationForecast.properties.timeseries[0].data.instant.details.wind_from_direction
        val windSpeed = locationForecast.properties.timeseries[0].data.instant.details.wind_speed

        return ForecastNextHour(
            symbolCode = symbolCode,
            airTemperature = airTemperature,
            precipitationAmount = precipitationAmount,
            windFromDirection = windFromDirection,
            windSpeed = windSpeed,
        )
    }


}