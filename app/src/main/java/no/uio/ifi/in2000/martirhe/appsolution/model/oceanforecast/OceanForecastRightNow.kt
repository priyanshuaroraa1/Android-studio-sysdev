package no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast

import com.google.gson.annotations.SerializedName
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
        return (seaSurfaceWaveHeight * 10).roundToInt().toString()
    }

    fun getWaterTemperatureString(): String {
        return seaWaterTemperature.roundToInt().toString()
    }
}
