package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextWeek
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast

sealed interface LocationForecastUiState {
    data class Success(
        val locationForecast: LocationForecast,
        val forecastNextHour: ForecastNextHour,
        val forecastNextWeek: ForecastNextWeek,
        ): LocationForecastUiState
    object Loading: LocationForecastUiState
    object Error: LocationForecastUiState
}