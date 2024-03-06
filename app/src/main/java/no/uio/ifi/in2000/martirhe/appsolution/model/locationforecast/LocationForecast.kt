package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

data class LocationForecast (
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry (
    val type: String,
    val coordinates: List<Double>
)

data class Properties (
    val meta: Meta,
    val timeseries: List<Timesery>
)

data class Meta (
    val updatedAt: String,
    val units: Units
)

data class Units (
    val airPressureAtSeaLevel: String,
    val airTemperature: String,
    val cloudAreaFraction: String,
    val precipitationAmount: String,
    val relativeHumidity: String,
    val windFromDirection: String,
    val windSpeed: String
)

data class Timesery (
    val time: String,
    val data: Data
)

data class Data (
    val instant: Instant,
    val next12_Hours: Next12_Hours? = null,
    val next1_Hours: NextHours? = null,
    val next6_Hours: NextHours? = null
)

data class Instant (
    val details: InstantDetails
)

data class InstantDetails (
    val airPressureAtSeaLevel: Double,
    val airTemperature: Double,
    val cloudAreaFraction: Double,
    val relativeHumidity: Double,
    val windFromDirection: Double,
    val windSpeed: Double
)

data class Next12_Hours (
    val summary: Summary,
    val details: Next12_HoursDetails
)

class Next12_HoursDetails()

data class Summary (
    val symbolCode: String
)

data class NextHours (
    val summary: Summary,
    val details: Next1_HoursDetails
)

data class Next1_HoursDetails (
    val precipitationAmount: Long
)