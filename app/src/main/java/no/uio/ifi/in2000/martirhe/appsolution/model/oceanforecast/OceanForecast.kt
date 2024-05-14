package no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast

import com.google.gson.annotations.SerializedName


data class OceanForecast (
    @SerializedName("type") val type : String,
    @SerializedName("geometry") val geometry : Geometry,
    @SerializedName("properties") val properties : Properties
)

data class Geometry (
    @SerializedName("type") val type : String,
    @SerializedName("coordinates") val coordinates : List<Double>
)

data class Properties (
    @SerializedName("meta") val meta : Meta,
    @SerializedName("timeseries") val timeseries : List<Timeseries>
)

data class Meta (
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("units") val units : Units
)

data class Units (
    @SerializedName("sea_surface_wave_from_direction") val seaSurfaceWaveFromDirection : String,
    @SerializedName("sea_surface_wave_height") val seaSurfaceWaveHeight : String,
    @SerializedName("sea_water_speed") val seaWaterSpeed : String,
    @SerializedName("sea_water_temperature") val seaWaterTemperature : String,
    @SerializedName("sea_water_to_direction") val seaWaterToDirection : String
)

data class Timeseries (
    @SerializedName("time") val time : String,
    @SerializedName("data") val data : Data
)

data class Data (
    @SerializedName("instant") val instant : Instant
)

data class Instant (
    @SerializedName("details") val details : Details
)

data class Details (
    @SerializedName("sea_surface_wave_from_direction") val seaSurfaceWaveFromDirection : Double,
    @SerializedName("sea_surface_wave_height") val seaSurfaceWaveHeight : Double,
    @SerializedName("sea_water_speed") val seaWaterSpeed : Double,
    @SerializedName("sea_water_temperature") val seaWaterTemperature : Double,
    @SerializedName("sea_water_to_direction") val seaWaterToDirection : Double
)

