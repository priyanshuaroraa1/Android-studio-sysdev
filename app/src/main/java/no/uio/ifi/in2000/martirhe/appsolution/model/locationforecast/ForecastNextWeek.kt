package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import java.time.LocalDate


data class ForecastNextWeek(
    val weekList: List<ForecastWeekday>,
)
data class ForecastWeekday(
    val date: LocalDate,
    val symbolCode: String,
    val airTemperature: String,
) {
    fun getWeekday(): String {
        return "UKEDAG"
    }
    // TODO: Skrive getTemperature:
}