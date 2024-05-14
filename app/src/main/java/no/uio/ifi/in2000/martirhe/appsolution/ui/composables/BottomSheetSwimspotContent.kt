package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.LocationForecastUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.MetAlertUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.OceanForecastState

@Composable
fun BottomSheetSwimspotContent(
    homeViewModel: HomeViewModel,
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    val outerEdgePaddingValues: Dp = dimensionResource(id = R.dimen.padding_medium)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        LazyColumn {

            if (homeState.selectedSwimspot != null) {
                item {

                    Column(
                        modifier = Modifier.padding(horizontal = outerEdgePaddingValues)

                    ) {
                        val metAlertStateNew = homeViewModel.metAlertUiState.collectAsState().value

                        metAlertStateNew.let { state ->
                            when (state) {
                                is MetAlertUiState.Success -> {
                                    val coordinates = homeState.selectedSwimspot.getLatLng()
                                    val simpleMetAlertList = state.simpleMetAlertList.filter {
                                        it.isRelevantForCoordinate(coordinates)
                                    }

                                    MetAlertCard(
                                        simpleMetAlertList = simpleMetAlertList,
                                        onClick = { homeViewModel.updateShowMetAlertDialog(true) },
                                    )
                                    homeViewModel.updateMetAlertDialogList(simpleMetAlertList)
                                }

                                is MetAlertUiState.Loading -> {
                                }

                                is MetAlertUiState.Error -> {
                                }
                            }
                        }

                        homeViewModel.locationForecastUiState.let { locationForecastUiState ->
                            when (locationForecastUiState) {
                                is LocationForecastUiState.Success -> {

                                    homeViewModel.oceanForecastUiState.let { oceanForecastState ->
                                        when (oceanForecastState) {
                                            is OceanForecastState.Success -> {
                                                WeatherNextHourCard(
                                                    forecastNextHour = locationForecastUiState.forecastNextHour,
                                                    oceanForecastRightNow = oceanForecastState.oceanForecastRightNow,
                                                    homeViewModel = homeViewModel,
                                                )
                                            }
                                            else -> {
                                                WeatherNextHourCard(
                                                    locationForecastUiState.forecastNextHour,
                                                    homeViewModel = homeViewModel,
                                                )
                                            }
                                        }

                                    }


                                }

                                is LocationForecastUiState.Loading -> {
                                    WeatherNextHourSkeletonLoading()


                                }

                                is LocationForecastUiState.Error -> {
                                    WeatherNextHourErrorCard()
                                }
                            }
                        }
                    }
                }

                item {
                    if (homeState.selectedSwimspot.url != null) {
                        SwimspotImage(
                            swimspot = homeState.selectedSwimspot
                        )
                    }
                }

                item {
                    homeViewModel.locationForecastUiState.let { locationForecastState ->
                        when (locationForecastState) {
                            is LocationForecastUiState.Success -> {

                                homeViewModel.oceanForecastUiState.let { oceanForecastState ->
                                    when (oceanForecastState) {
                                        is OceanForecastState.Success -> {
                                            WeatherNextWeekCard(
                                                outerEdgePaddingValues = outerEdgePaddingValues,
                                                forecastNextWeek = locationForecastState.forecastNextWeek,
                                                oceanForecastRightNow = oceanForecastState.oceanForecastRightNow,
                                            )
                                        }

                                        else -> {
                                            WeatherNextWeekCard(
                                                outerEdgePaddingValues = outerEdgePaddingValues,
                                                forecastNextWeek = locationForecastState.forecastNextWeek,
                                                oceanForecastRightNow = null,
                                            )
                                        }
                                    }
                                }


                            }

                            is LocationForecastUiState.Loading -> {
                                WeatherNextWeekSkeletonLoading()
                            }

                            is LocationForecastUiState.Error -> {
                            }
                        }
                    }
                }

                item {
                    if (homeState.selectedSwimspot.accessibility != null) {
                        AccessibilityOptionsCard(
                            accessibilityStringList = homeState.selectedSwimspot.getAccecibilityStrings(),
                            homeViewModel = homeViewModel,
                            outerEdgePaddingValues = outerEdgePaddingValues
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large))
                    )
                }
            }
        }
    }
}