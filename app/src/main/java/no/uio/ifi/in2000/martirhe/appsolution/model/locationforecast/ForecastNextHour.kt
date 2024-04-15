package no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast

data class ForecastNextHour(
    val symbolCode: String?,
    val airTemperature: Double?,
    val precipitationAmount: Double?,
    val windFromDirection: Double,
    val windSpeed: Double,
)