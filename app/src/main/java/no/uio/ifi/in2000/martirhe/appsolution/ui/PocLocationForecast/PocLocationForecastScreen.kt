package no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


import no.uio.ifi.in2000.martirhe.appsolution.ui.locationpermissionrationaledialog.PermissionRationaleDialog
import no.uio.ifi.in2000.martirhe.appsolution.ui.locationpermissionrationaledialog.RationaleState
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.FarevarselUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselViewModel
import kotlin.math.roundToInt


//@RequiresApi(Build.VERSION_CODES.Q)
//@Preview(
//    name = "Dark Mode",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Preview(
//    name = "Light Mode",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun PocLocationForecastScreenPreview() {
//    PocLocationForecastScreen()
//}


@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PocLocationForecastScreen(
    pocLocationForecastViewModel: PocLocationForecastViewModel = viewModel(),
) {
    // TODO: Skal denne ligge her eller i ViewModel?
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var locationInfo by remember {
        mutableStateOf(" ??? ")
    }

    // TODO: Skal denne ligge her eller i ViewModel?
    // Approximate location access is sufficient for most of use cases
    val coarseLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    // TODO: Skal denne ligge her eller i ViewModel?
    // When precision is important request both permissions but make sure to handle the case where
    // the user only grants ACCESS_COARSE_LOCATION
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )

    // TODO: Skal denne ligge her eller i ViewModel?
//    // In really rare use cases, accessing background location might be needed.
//    val bgLocationPermissionState = rememberPermissionState(
//        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//    )

    // TODO: Skal denne ligge her eller i ViewModel?
    // Keeps track of the rationale dialog state, needed when the user requires further rationale
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        // Show rationale dialog when needed
        rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

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

        Button(onClick = {

            if (coarseLocationPermissionState.status.isGranted) {
                Log.i("permission", "Location permission IS granted")

                scope.launch(Dispatchers.IO) {
                    val usePreciseLocation: Boolean = false;
                    val priority = if (usePreciseLocation) {
                        Priority.PRIORITY_HIGH_ACCURACY
                    } else {
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    }
                    val result = locationClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token,
                    ).await()
                    result?.let { fetchedLocation ->
                        pocLocationForecastViewModel.latitude = fetchedLocation.latitude
                        pocLocationForecastViewModel.longitude = fetchedLocation.longitude
                        pocLocationForecastViewModel.showForecast = true
                        pocLocationForecastViewModel.chosenCity = "min posisjon"
                        pocLocationForecastViewModel.loadLocationForecast(
                            pocLocationForecastViewModel.latitude,
                            pocLocationForecastViewModel.longitude
                        )
                    }
                }
            } else if (coarseLocationPermissionState.status.shouldShowRationale) {
                Log.i("permission", "Should show rationale")

                rationaleState = RationaleState(
                    "Request approximate location access",
                    "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
                ) { proceed ->
                    if (proceed) {

                        coarseLocationPermissionState.launchPermissionRequest()
                    }
                    rationaleState = null
                }
            } else {
                Log.i("permission", "Location permission is NOT granted")
                coarseLocationPermissionState.launchPermissionRequest()
            }

            Log.i("location", locationInfo)

        }) {
            Text(text = "Min posisjon")
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
                                    text = state.locationForecast.properties.timeseries[0].data.instant.details.air_temperature.roundToInt()
                                        .toString() + " grader",
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
                                Text(
                                    text = "Koordinater",
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Text(
                                    text = "Lat:" + pocLocationForecastViewModel.latitude,
                                )
                                Text(
                                    text = "Lon:" + pocLocationForecastViewModel.longitude,
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





