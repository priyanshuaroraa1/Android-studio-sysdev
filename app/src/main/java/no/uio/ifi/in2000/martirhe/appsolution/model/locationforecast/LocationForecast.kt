package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

import com.google.gson.annotations.SerializedName



data class LocationForecast (
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
    @SerializedName("air_pressure_at_sea_level") val airPressureAtSeaLevel : String,
    @SerializedName("air_temperature") val airTemperature : String,
    @SerializedName("cloud_area_fraction") val cloudAreaFraction : String,
    @SerializedName("precipitation_amount") val precipitationAmount : String,
    @SerializedName("relative_humidity") val relativeHumidity : String,
    @SerializedName("wind_from_direction") val windFromDirection : String,
    @SerializedName("wind_speed") val windSpeed : String
)

data class Timeseries (
    @SerializedName("time") val time : String,
    @SerializedName("data") val data : Data
)

data class Data (
    val instant: Instant,
    @SerializedName("next_12_hours") val next12Hours: Next12Hours? = null,
    @SerializedName("next_1_hours") val next1Hours: Next1Hours? = null,
    @SerializedName("next_6_hours") val next6Hours: Next6Hours? = null
)

data class Instant (
    @SerializedName("details") val details : InstantDetails
)

data class InstantDetails (
    @SerializedName("air_pressure_at_sea_level") val airPressureAtSeaLevel: Double,
    @SerializedName("air_temperature") val airTemperature: Double,
    @SerializedName("cloud_area_fraction") val cloudAreaFraction: Double,
    @SerializedName("relative_humidity") val relativeHumidity: Double,
    @SerializedName("wind_from_direction") val windFromDirection: Double,
    @SerializedName("wind_speed") val windSpeed: Double
)

data class Next12Hours (
    @SerializedName("summary") val summary: Summary,
    @SerializedName("details") val details: Next12HoursDetails
)
data class Next6Hours (
    val summary: Summary,
    val details: Next6HoursDetails
)
data class Next1Hours (
    val summary: Summary,
    val details: Next1HoursDetails
)

class Next12HoursDetails()

data class Next1HoursDetails(
    @SerializedName("precipitation_amount") val precipitationAmount : Double
)
data class Next6HoursDetails(
    @SerializedName("precipitation_amount") val precipitationAmount : Double
)

data class Summary (
    @SerializedName("symbol_code") val symbolCode : String
)


