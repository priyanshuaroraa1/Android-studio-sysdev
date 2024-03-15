package no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast

import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.Properties

data class OceanForecast (
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry (
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val meta: Meta,
    val timeseries: List<Timeseries>
)

data class Meta(
    val updated_at: String,
    val units: Units
)

data class Units(
    val sea_surface_wave_from_direction: String,
    val sea_surface_wave_height: String,
    val sea_water_speed: String,
    val sea_water_temperature: String,
    val sea_water_to_direction: String
)

data class Timeseries(
    val time: String,
    val data: Data
)

data class Data(
    val instant: Instant
)

data class Instant(
    val details: Details
)

data class Details(
    val sea_surface_wave_from_direction: Double,
    val sea_surface_wave_height: Double,
    val sea_water_speed: Double,
    val sea_water_temperature: Double,
    val sea_water_to_direction: Double
)

