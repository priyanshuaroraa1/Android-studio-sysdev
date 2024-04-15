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
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("units") val units : Units
)

data class Units (
    @SerializedName("air_pressure_at_sea_level") val air_pressure_at_sea_level : String,
    @SerializedName("air_temperature") val air_temperature : String,
    @SerializedName("cloud_area_fraction") val cloud_area_fraction : String,
    @SerializedName("precipitation_amount") val precipitation_amount : String,
    @SerializedName("relative_humidity") val relative_humidity : String,
    @SerializedName("wind_from_direction") val wind_from_direction : String,
    @SerializedName("wind_speed") val wind_speed : String
)

data class Timeseries (
    @SerializedName("time") val time : String,
    @SerializedName("data") val data : Data
)

data class Data (
    val instant: Instant,
    val next12_Hours: Next12_Hours? = null,
    val next_1_hours: Next_1_Hours? = null,
    val next6_Hours: NextHours? = null
)

data class Instant (
    @SerializedName("details") val details : InstantDetails
)

data class InstantDetails (
    @SerializedName("air_pressure_at_sea_level") val air_pressure_at_sea_level: Double,
    @SerializedName("air_temperature") val air_temperature: Double,
    @SerializedName("cloud_area_fraction") val cloud_area_fraction: Double,
    @SerializedName("relative_humidity") val relative_humidity: Double,
    @SerializedName("wind_from_direction") val wind_from_direction: Double,
    @SerializedName("wind_speed") val wind_speed: Double
)

data class Next12_Hours (
    val summary: Summary,
    @SerializedName("details") val details: Next_12_Hours_Details
)

class Next_12_Hours_Details()


data class Next_1_Hours (
    val summary: Summary,
    val details: Next_1_Hours_Details
)
data class Next_1_Hours_Details(
    @SerializedName("precipitation_amount") val precipitation_amount : Double
)

data class Summary (
    @SerializedName("symbol_code") val symbol_code : String
)

data class NextHours (
    val summary: Summary,
    val details: Next1_HoursDetails
)

data class Next1_HoursDetails (
    val precipitationAmount: Double
)