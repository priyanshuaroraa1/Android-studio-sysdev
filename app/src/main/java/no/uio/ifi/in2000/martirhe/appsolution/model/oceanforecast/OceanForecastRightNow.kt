package no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast

import kotlin.math.roundToInt

data class OceanForecastRightNow (
    val seaSurfaceWaveFromDirection : Double,
    val seaSurfaceWaveHeight : Double,
    val seaWaterSpeed : Double,
    val seaWaterTemperature : Double,
    val seaWaterToDirection : Double,
    val isSaltWater : Boolean,
) {
    fun getWaveHeightString(): String {
        return (seaSurfaceWaveHeight * 100).roundToInt().toString()
    }

    fun getWaterTemperatureString(): String {
        return seaWaterTemperature.roundToInt().toString()
    }

    fun getWaveDirectionString(): String {
        return when {
            seaSurfaceWaveFromDirection < 0 -> "Invalid direction"
            seaSurfaceWaveFromDirection >= 348.75 || seaSurfaceWaveFromDirection < 11.25 -> "north"
            seaSurfaceWaveFromDirection >= 11.25 && seaSurfaceWaveFromDirection < 33.75 -> "north_northeast"
            seaSurfaceWaveFromDirection >= 33.75 && seaSurfaceWaveFromDirection < 56.25 -> "northeast"
            seaSurfaceWaveFromDirection >= 56.25 && seaSurfaceWaveFromDirection < 78.75 -> "east_northeast"
            seaSurfaceWaveFromDirection >= 78.75 && seaSurfaceWaveFromDirection < 101.25 -> "east"
            seaSurfaceWaveFromDirection >= 101.25 && seaSurfaceWaveFromDirection < 123.75 -> "east_southeast"
            seaSurfaceWaveFromDirection >= 123.75 && seaSurfaceWaveFromDirection < 146.25 -> "southeast"
            seaSurfaceWaveFromDirection >= 146.25 && seaSurfaceWaveFromDirection < 168.75 -> "south_southeast"
            seaSurfaceWaveFromDirection >= 168.75 && seaSurfaceWaveFromDirection < 191.25 -> "south"
            seaSurfaceWaveFromDirection >= 191.25 && seaSurfaceWaveFromDirection < 213.75 -> "south_southwest"
            seaSurfaceWaveFromDirection >= 213.75 && seaSurfaceWaveFromDirection < 236.25 -> "southwest"
            seaSurfaceWaveFromDirection >= 236.25 && seaSurfaceWaveFromDirection < 258.75 -> "west_southwest"
            seaSurfaceWaveFromDirection >= 258.75 && seaSurfaceWaveFromDirection < 281.25 -> "west"
            seaSurfaceWaveFromDirection >= 281.25 && seaSurfaceWaveFromDirection < 303.75 -> "west_northwest"
            seaSurfaceWaveFromDirection >= 303.75 && seaSurfaceWaveFromDirection < 326.25 -> "northwest"
            seaSurfaceWaveFromDirection >= 326.25 && seaSurfaceWaveFromDirection < 348.75 -> "north_northwest" // Warning: Not changed because of readibility
            else -> "Invalid direction"
        }
    }
}
