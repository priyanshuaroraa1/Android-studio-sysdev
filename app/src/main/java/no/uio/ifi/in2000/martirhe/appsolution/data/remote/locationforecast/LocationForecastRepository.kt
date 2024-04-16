package no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextWeek
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastWeekday
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject


interface LocationForecastRepositoryInterface {
    suspend fun getLocationForecast(lat: Double, lon: Double): LocationForecast
    suspend fun getForecastNextHour(lat: Double, lon: Double): ForecastNextHour
    suspend fun getForecastNextWeek(lat: Double, lon: Double): ForecastNextWeek
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForecastNextWeek(lat: Double, lon: Double): ForecastNextWeek {
        val locationForecast = getLocationForecast(lat, lon)
        val forecastMap = locationForecast.properties.timeseries.associateBy {
            ZonedDateTime.parse(it.time)
        }

        // Zone setup
        val zoneId = ZoneId.of("Z") // Adjust if needed
        val tomorrow = ZonedDateTime.now(zoneId).toLocalDate().plusDays(1).atStartOfDay(zoneId)

        // Generate 06:00 and 18:00 keys for the next 7 days
        val validTimes = (0 until 7).flatMap { dayIndex ->
            listOf(tomorrow.withHour(6).plusDays(dayIndex.toLong()), tomorrow.withHour(18).plusDays(dayIndex.toLong()))
        }.toSet()

        // Filter to keep only valid times
        val validForecasts = forecastMap.filterKeys { it in validTimes }

        val listOfWeekdayForecast = validTimes.sorted().chunked(2).mapNotNull { (morning, evening) ->
            val morningForecast = validForecasts[morning]
            val eveningForecast = validForecasts[evening]
            if (morningForecast != null && eveningForecast != null) {
                ForecastWeekday(
                    date = morning.toLocalDate(),
                    symbolCode = morningForecast.data.next_12_hours?.summary?.symbol_code ?: "unknown",
                    airTemperature = eveningForecast.data.instant.details.air_temperature.toString()
                )
            } else null
        }

        listOfWeekdayForecast.forEach { forecast ->
            Log.i("Timeseries", "${forecast.date}\n${forecast.airTemperature}\n${forecast.symbolCode}\n\n")
        }

        return ForecastNextWeek(
            weekList = listOfWeekdayForecast
        )
    }
}