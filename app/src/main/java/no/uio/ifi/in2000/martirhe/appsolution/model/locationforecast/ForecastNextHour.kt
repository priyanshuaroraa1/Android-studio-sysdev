package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.martirhe.appsolution.R
import kotlin.math.roundToInt

// Simplified data class by removing unnecessary null checks as they are not needed in this context.
data class ForecastNextHour(
    val symbolCode: String,
    val airTemperature: Double,
    val precipitationAmount: Double,
    val windFromDirection: Double,
    val windSpeed: Double,
) {
    // Removed redundant function calls by directly using the properties where applicable.
    fun getTemperatureString(): String = roundIfNotNull(airTemperature).toString()

    fun getWindSpeedString(): String = roundIfNotNull(windSpeed).toString()

    private fun roundIfNotNull(value: Double?) = value?.let { it.roundToInt() } ?: "N/A"

    fun getWindDirectionString(): String = calculateWindDirection(windFromDirection)

    companion object {
        // Moved the mapping of symbols to icons into a separate function to avoid repetition.
        private fun createIconForSymbol(symbolCode: String): Painter {
            return painterResource(id = when (symbolCode) {
                "clear-day", "clear-night" -> R.drawable.ic_weather_sunny
                "rain", "snow", "partly-cloudy-day", "partly-cloudy-night" -> R.drawable.ic_weather_cloudy
                "thunderstorm" -> R.drawable.ic_weather_lightning
                "tornado" -> R.drawable.ic_weather_hurricane
                "fog" -> R.drawable.ic_weather_fog
                "wind" -> R.drawable.ic_weather_wind
                else -> R.drawable.ic_weather_cloudy
            })
        }

        // Created an extension function to handle conversion from symbol codes to drawables.
        fun convertSymbolCodeToWeatherIcon(symbolCode: String): Drawable {
            return createIconForSymbol(symbolCode).toAndroidBitmap()
        }
    }
}

private const val NORTH = 0.0..22.5
private const val NORTHEAST = 22.5..67.5
private const val EAST = 67.5..112.5
private const val SOUTHEAST = 112.5..157.5
private const val SOUTH = 157.5..202.5
private const val SOUTHWEST = 202.5..247.5
private const val WEST = 247.5..292.5
private const val NORTHWEST = 292.5..337.5

private fun calculateWindDirection(directionInDegrees: Double): String {
    val normalizedDirection = ((directionInDegrees + 360) % 360 / 360 * 4).coerceAtMost(3)
    return when (normalizedDirection) {
        in NORTH -> "north"
        in NORTHEAST -> "north_northeast"
        in EAST -> "east_northeast"
        in SOUTHEAST -> "east"
        in SOUTH -> "south"
        in SOUTHWEST -> "south_southeast"
        in WEST -> "west"
        in NORTHWEST -> "north_northwest"
        else -> "invalid"
    }
}