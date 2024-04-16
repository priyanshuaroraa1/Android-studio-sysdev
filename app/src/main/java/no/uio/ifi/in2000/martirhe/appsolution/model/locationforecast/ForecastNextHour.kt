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
    fun getTemperatureString(): String {
        return if (airTemperature == null) {
            "N/A"
        } else {
            airTemperature.roundToInt().toString()
        }
    }

    fun getWindSpeedString(): String {
        return if (windSpeed == null) {
            "N/A"
        } else {
            windSpeed.roundToInt().toString()
        }
    }

    fun getWindDirectionString(): String {
        return when {
            windFromDirection < 0 -> "Invalid direction"
            windFromDirection >= 348.75 || windFromDirection < 11.25 -> "north"
            windFromDirection >= 11.25 && windFromDirection < 33.75 -> "north_northeast"
            windFromDirection >= 33.75 && windFromDirection < 56.25 -> "northeast"
            windFromDirection >= 56.25 && windFromDirection < 78.75 -> "east_northeast"
            windFromDirection >= 78.75 && windFromDirection < 101.25 -> "east"
            windFromDirection >= 101.25 && windFromDirection < 123.75 -> "east_southeast"
            windFromDirection >= 123.75 && windFromDirection < 146.25 -> "southeast"
            windFromDirection >= 146.25 && windFromDirection < 168.75 -> "south_southeast"
            windFromDirection >= 168.75 && windFromDirection < 191.25 -> "south"
            windFromDirection >= 191.25 && windFromDirection < 213.75 -> "south_southwest"
            windFromDirection >= 213.75 && windFromDirection < 236.25 -> "southwest"
            windFromDirection >= 236.25 && windFromDirection < 258.75 -> "west_southwest"
            windFromDirection >= 258.75 && windFromDirection < 281.25 -> "west"
            windFromDirection >= 281.25 && windFromDirection < 303.75 -> "west_northwest"
            windFromDirection >= 303.75 && windFromDirection < 326.25 -> "northwest"
            windFromDirection >= 326.25 && windFromDirection < 348.75 -> "north_northwest"
            else -> "Invalid direction"
        }
    }



}