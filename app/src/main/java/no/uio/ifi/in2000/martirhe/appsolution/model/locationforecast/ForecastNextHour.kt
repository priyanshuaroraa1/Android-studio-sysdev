package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.martirhe.appsolution.R
import kotlin.math.roundToInt

data class ForecastNextHour(
    val symbolCode: String?,
    val airTemperature: Double?,
    val precipitationAmount: Double?,
    val windFromDirection: Double,
    val windSpeed: Double,
) {
    fun getTemperatureString(): String =
        airTemperature?.roundToInt()?.toString() ?: "N/A"

    fun getWindSpeedString(): String = windSpeed.roundToInt().toString()

    fun getWindDirectionString(): String {
        if (windFromDirection < 0 || windFromDirection >= 360) {
            return "Invalid direction"
        }

        val directionRanges = mapOf(
            348.75f..360.0f to "north",
            0.0f..11.25f to "north",
            11.25f..33.75f to "north_northeast",
            33.75f..56.25f to "northeast",
            56.25f..78.75f to "east_northeast",
            78.75f..101.25f to "east",
            101.25f..123.75f to "east_southeast",
            123.75f..146.25f to "southeast",
            146.25f..168.75f to "south_southeast",
            168.75f..191.25f to "south",
            191.25f..213.75f to "south_southwest",
            213.75f..236.25f to "southwest",
            236.25f..258.75f to "west_southwest",
            258.75f..281.25f to "west",
            281.25f..303.75f to "west_northwest",
            303.75f..326.25f to "northwest",
            326.25f..348.75f to "north_northwest"
        )

        return directionRanges.entries.firstOrNull { windFromDirection in it.key }?.value ?: "Invalid direction"
    }



}