package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow

sealed interface OceanForecastState {
    data class Success(
        val oceanForecastRightNow: OceanForecastRightNow,
    ): OceanForecastState

    data object Loading: OceanForecastState
    data object Error: OceanForecastState
}