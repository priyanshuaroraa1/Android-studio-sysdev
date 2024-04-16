package no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast

import android.os.Build
import android.text.format.Time
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
        val allTimeseries = locationForecast.properties.timeseries

        val forecastMap = allTimeseries.associateBy {
            ZonedDateTime.parse(it.time)
        }

        // Zone setup
        val zoneId = ZoneId.of("Z") // Adjust if needed
        val now = ZonedDateTime.now(zoneId)
        val tomorrow = now.toLocalDate().plusDays(1).atStartOfDay(zoneId)

        // Generating 06:00 and 18:00 keys for the next 7 days
        val validDays06 = (0 until 7).map { dayIndex ->
            tomorrow.withHour(6).plusDays(dayIndex.toLong())
        }.toSet()
        val validDays18 = (0 until 7).map { dayIndex ->
            tomorrow.withHour(18).plusDays(dayIndex.toLong())
        }.toSet()

        // Filter to keep only the next 7 days starting from tomorrow at 06:00 and 18:00
        val nextSevenDaysMap06 = forecastMap.filterKeys {
            validDays06.contains(it)
        }
        val nextSevenDaysMap18 = forecastMap.filterKeys {
            validDays18.contains(it)
        }

        var listOfWeekdayForecast: MutableList<ForecastWeekday> = mutableListOf()

        for (date06 in validDays06) {
            val timeseries06 = nextSevenDaysMap06[date06]
            val timeseries18 = nextSevenDaysMap18[date06.withHour(18)]

            if (timeseries06 != null && timeseries18 != null) {
                listOfWeekdayForecast.add(
                    ForecastWeekday(
                        date = date06,
                        symbolCode = timeseries06.data.next_12_hours?.summary?.symbol_code ?: "unknown",
                        airTemperature = timeseries18.data.instant.details.air_temperature.toString()
                    )
                )
            }
        }

        for (d in listOfWeekdayForecast) {
            Log.i("Timeseries", d.date.toString() + "\n" + d.airTemperature + "\n" + d.symbolCode + "\n\n")
        }

        return ForecastNextWeek(
            weekList = listOfWeekdayForecast
        )
    }
}