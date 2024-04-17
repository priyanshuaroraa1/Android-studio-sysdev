package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow

sealed interface OceanForecastState {
    data class Success(
        val oceanForecast: OceanForecast,
        val oceanForecastRightNow: OceanForecastRightNow,
    ): OceanForecastState

    object Loading: OceanForecastState
    object Error: OceanForecastState
}