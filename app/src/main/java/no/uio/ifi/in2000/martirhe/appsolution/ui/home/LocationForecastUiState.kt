package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast

sealed interface LocationForecastUiState {
    data class Success(val locationForecast: LocationForecast): LocationForecastUiState
    object Loading: LocationForecastUiState
    object Error: LocationForecastUiState
}