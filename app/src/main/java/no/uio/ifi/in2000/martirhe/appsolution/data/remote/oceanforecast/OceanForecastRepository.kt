package no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalDistance
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow
import javax.inject.Inject

interface OceanForecastRepositoryInterface {
    suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast
    suspend fun getOceanForecastRightNow(lat: Double, lon: Double): OceanForecastRightNow
}

class OceanForecastRepository @Inject constructor(
    private val dataSource: OceanForecastDataSource
) : OceanForecastRepositoryInterface {

    override suspend fun getOceanForecast(lat: Double, lon: Double): OceanForecast {
        return dataSource.fetchOceanForecast(lat, lon)
    }

    override suspend fun getOceanForecastRightNow(lat: Double, lon: Double): OceanForecastRightNow {
        val oceanForecast = getOceanForecast(lat, lon)

        val swimspotCoordinates = LatLng(lat, lon)
        val nearestCoastlineCoordinates = LatLng(
            oceanForecast.geometry.coordinates[1],
            oceanForecast.geometry.coordinates[0]
        )
        val distanceToCoastThreshold = 2000.0
        val distanceToCoast = swimspotCoordinates.sphericalDistance(nearestCoastlineCoordinates)
        val isSaltWater = distanceToCoast < distanceToCoastThreshold

        val seaSurfaceWaveFromDirection = oceanForecast.properties.timeseries[0].data.instant.details.sea_surface_wave_from_direction
        val seaSurfaceWaveHeight = oceanForecast.properties.timeseries[0].data.instant.details.sea_surface_wave_height
        val seaWaterSpeed = oceanForecast.properties.timeseries[0].data.instant.details.sea_water_speed
        val seaWaterTemperature = oceanForecast.properties.timeseries[0].data.instant.details.sea_water_temperature
        val seaWaterToDirection = oceanForecast.properties.timeseries[0].data.instant.details.sea_water_to_direction


        return OceanForecastRightNow(
            seaSurfaceWaveFromDirection = seaSurfaceWaveFromDirection,
            seaSurfaceWaveHeight = seaSurfaceWaveHeight,
            seaWaterSpeed = seaWaterSpeed,
            seaWaterTemperature = seaWaterTemperature,
            seaWaterToDirection = seaWaterToDirection,
            isSaltWater = isSaltWater,
        )
    }
}

