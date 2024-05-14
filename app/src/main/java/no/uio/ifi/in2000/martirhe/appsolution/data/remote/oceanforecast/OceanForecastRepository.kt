package no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalDistance
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow
import javax.inject.Inject

interface OceanForecastRepositoryInterface {
    suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast
    suspend fun getOceanForecastRightNow(oceanForecast: OceanForecast, lat: Double, lon: Double): OceanForecastRightNow
}

class OceanForecastRepository @Inject constructor(
    private val dataSource: OceanForecastDataSource
) : OceanForecastRepositoryInterface {

    override suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast {
        try {
            return dataSource.fetchOceanForecast(lat, lon)
        } catch (e: Exception) {
            Log.e("OceanForecastDataSource", "Failed to fetch ocean forecast", e)
            throw e
        }
    }

    override suspend fun getOceanForecastRightNow(oceanForecast: OceanForecast, lat: Double, lon: Double): OceanForecastRightNow {
        return try {

            val swimspotCoordinates = LatLng(lat, lon)
            val nearestCoastlineCoordinates = LatLng(
                oceanForecast.geometry.coordinates[1],
                oceanForecast.geometry.coordinates[0]
            )
            val distanceToCoastThreshold = 2000.0
            val distanceToCoast = swimspotCoordinates.sphericalDistance(nearestCoastlineCoordinates)
            val isSaltWater = distanceToCoast < distanceToCoastThreshold

            val seaSurfaceWaveFromDirection = oceanForecast.properties.timeseries[0].data.instant.details.seaSurfaceWaveFromDirection
            val seaSurfaceWaveHeight = oceanForecast.properties.timeseries[0].data.instant.details.seaSurfaceWaveHeight
            val seaWaterSpeed = oceanForecast.properties.timeseries[0].data.instant.details.seaWaterSpeed
            val seaWaterTemperature = oceanForecast.properties.timeseries[0].data.instant.details.seaWaterTemperature
            val seaWaterToDirection = oceanForecast.properties.timeseries[0].data.instant.details.seaWaterToDirection


            OceanForecastRightNow(
                seaSurfaceWaveFromDirection = seaSurfaceWaveFromDirection,
                seaSurfaceWaveHeight = seaSurfaceWaveHeight,
                seaWaterSpeed = seaWaterSpeed,
                seaWaterTemperature = seaWaterTemperature,
                seaWaterToDirection = seaWaterToDirection,
                isSaltWater = isSaltWater,
            )
        } catch (e: Exception) {
            throw e
        }
    }
}

