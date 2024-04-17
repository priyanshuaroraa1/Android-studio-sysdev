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
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("units") val units : Units
)

data class Units (
    @SerializedName("sea_surface_wave_from_direction") val sea_surface_wave_from_direction : String,
    @SerializedName("sea_surface_wave_height") val sea_surface_wave_height : String,
    @SerializedName("sea_water_speed") val sea_water_speed : String,
    @SerializedName("sea_water_temperature") val sea_water_temperature : String,
    @SerializedName("sea_water_to_direction") val sea_water_to_direction : String
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
    @SerializedName("sea_surface_wave_from_direction") val sea_surface_wave_from_direction : Double,
    @SerializedName("sea_surface_wave_height") val sea_surface_wave_height : Double,
    @SerializedName("sea_water_speed") val sea_water_speed : Double,
    @SerializedName("sea_water_temperature") val sea_water_temperature : Double,
    @SerializedName("sea_water_to_direction") val sea_water_to_direction : Double
)

