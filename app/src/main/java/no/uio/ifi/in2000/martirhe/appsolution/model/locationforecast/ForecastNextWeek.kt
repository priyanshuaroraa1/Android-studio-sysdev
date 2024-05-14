package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle
import kotlin.math.roundToInt


data class ForecastNextWeek(
    val weekList: List<ForecastWeekday>,
)
data class ForecastWeekday(
    val date: LocalDate,
    val symbolCode: String,
    val airTemperature: Double,
) {
    fun getWeekdayString(): String {
        // Get today's date
        val today = LocalDate.now()

        // Check if the given date is tomorrow
        return if (date == today.plusDays(1)) {
            "I morgen"
        } else {
            // Get the name of the weekday in Norwegian, ensure only the first letter is capitalized
            val weekday = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("no", "NO"))
            weekday.substring(0, 1).uppercase() + weekday.substring(1).lowercase()
        }
    }
    fun getTemperatureString(): String {
        return airTemperature.roundToInt().toString()
    }
}