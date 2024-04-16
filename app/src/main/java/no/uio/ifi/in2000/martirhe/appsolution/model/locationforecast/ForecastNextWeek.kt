package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import java.time.ZonedDateTime


data class ForecastNextWeek(
    val weekList: List<ForecastFullWeekday>,
)
data class ForecastFullWeekday (
    val date: ZonedDateTime,
    val symbolCode: String,
    val airTemperature: String,
) {
    fun getWeekday(): String {
        return "UKEDAG"
    }
}