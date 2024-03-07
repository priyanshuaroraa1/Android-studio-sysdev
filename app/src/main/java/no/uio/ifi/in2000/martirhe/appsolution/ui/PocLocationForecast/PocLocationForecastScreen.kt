package no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.FarevarselUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselViewModel
import kotlin.math.roundToInt


@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PocLocationForecastScreenPreview() {
    PocLocationForecastScreen()
}

@Composable
fun PocLocationForecastScreen(
    pocLocationForecastViewModel: PocLocationForecastViewModel = viewModel(),
) {


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Press a button to display the weather.",
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                pocLocationForecastViewModel.latitude = 59.911491
                pocLocationForecastViewModel.longitude = 10.757933
                pocLocationForecastViewModel.showForecast = true
                pocLocationForecastViewModel.chosenCity = "Oslo"
                pocLocationForecastViewModel.loadLocationForecast(
                    pocLocationForecastViewModel.latitude,
                    pocLocationForecastViewModel.longitude
                )
            }
        ) {
            Text(text = "Oslo")
        }

        Button(
            onClick = {
                pocLocationForecastViewModel.latitude = 60.39299
                pocLocationForecastViewModel.longitude = 5.32415
                pocLocationForecastViewModel.showForecast = true
                pocLocationForecastViewModel.chosenCity = "Bergen"
                pocLocationForecastViewModel.loadLocationForecast(
                    pocLocationForecastViewModel.latitude,
                    pocLocationForecastViewModel.longitude
                )
            }
        ) {
            Text(text = "Bergen")
        }

        if (pocLocationForecastViewModel.showForecast) {

            val uiState by pocLocationForecastViewModel.uiState.collectAsState()

            uiState.locationForecastUiState.let { state ->
                when (state) {

                    is LocationForecastUiState.Success -> {

                        Card(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Været for " + pocLocationForecastViewModel.chosenCity,
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Text(
                                    text = state.locationForecast.properties.timeseries[0].time,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )


                                Text(
                                    text = "Temperatur",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Text(
                                    text = state.locationForecast.properties.timeseries[0].data.instant.details.air_temperature.roundToInt().toString() + " grader",
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )



                                Text(
                                    text = "Skydekke",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Text(
                                    text = state.locationForecast.properties.timeseries[0].data.instant.details.cloud_area_fraction.toString(),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )


                                Text(
                                    text = "Vindhastighet",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Text(
                                    text = state.locationForecast.properties.timeseries[0].data.instant.details.wind_speed.toString() + " m/s",
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }


                        }

                    }

                    is LocationForecastUiState.Loading -> {
                        Text(text = "Loading")
                    }

                    is LocationForecastUiState.Error -> {
                        Text(text = "Error")
                    }
                }
            }
        }
    }
}


