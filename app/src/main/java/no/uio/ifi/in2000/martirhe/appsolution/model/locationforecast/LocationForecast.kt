package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

//TODO: Endre navn på variabler til at det er likt som JSON eller endre det

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
    val air_temperature: String,    //S: endret navn etter hva det heter i JSON-fil
    val cloud_area_fraction: String,    //S: endret navn etter hva det heter i JSON-fil
    val precipitationAmount: String,
    val relativeHumidity: String,
    val windFromDirection: String,
    val wind_speed: String  //S: endret navn etter hva det heter i JSON-fil
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
    val air_temperature: Double, //S: endret navn etter hva det heter i JSON-fil
    val cloud_area_fraction: Double, //S: endret navn etter hva det heter i JSON-fil
    val relativeHumidity: Double,
    val windFromDirection: Double,
    val wind_speed: Double //S: endret navn etter hva det heter i JSON-fil
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