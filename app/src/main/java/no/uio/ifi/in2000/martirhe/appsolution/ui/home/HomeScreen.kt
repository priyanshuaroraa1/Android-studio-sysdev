package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material3.BottomSheetScaffold

import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.local.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.ui.home.composables.HomeSearchBar
import no.uio.ifi.in2000.martirhe.appsolution.util.UiEvent
import java.util.Locale


@SuppressLint("PotentialBehaviorOverride")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    val cameraPositionState = rememberCameraPositionState {
        position = homeState.defaultCameraPosition
    }


    // TODO: Flytte dette til homeState?
    val mapStyleString = loadMapStyleFromAssets()
    val mapProperties = MapProperties(
        isMyLocationEnabled = false,
        mapStyleOptions = MapStyleOptions(mapStyleString)
    )

    // Obtain a coroutine scope tied to the lifecycle of this composable
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {

            BottomSheetSwimspotContent(
                homeViewModel = homeViewModel
            )
        },
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(40.dp),
                    imageVector = Icons.Default.Maximize,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    contentDescription = "Handle"
                )

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }

            }

        },
        sheetPeekHeight = homeState.bottomSheetPosition.heightDp
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HomeSearchBar(homeViewModel = homeViewModel)

            GoogleMap(
                modifier = Modifier,
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    homeViewModel.onMapBackroundClick(it)
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.expand()
                            Log.i("Sheet", "expand")
                        }
                },
                properties = mapProperties
            ) {

                MapEffect() { map ->


                    map.setOnMarkerClickListener { marker ->
                        // Dette skjer når en Marker blir klikket på:
                        val swimspot = marker.tag as? Swimspot // Cast the tag to your data type
                        Log.i("Marker tag cast:", swimspot.toString())
                        if (swimspot != null) {
                            homeViewModel.onSwimspotPinClick(swimspot)
                        }

                        Log.i("Marker tag:", marker.tag.toString())
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLng(marker.position),
                                durationMs = 250
                            )
                        }
                        // Expand BottomSheet
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }

                        true // Return true to indicate that the click event has been handled
                    }

                    homeState.allSwimspots.forEach { swimspot ->
                        val marker = map.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    swimspot.lat,
                                    swimspot.lon
                                )
                            )
                        )
                        marker?.tag = swimspot
                    }

                    if (homeState.customSwimspot != null) {
                        val marker = map.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    homeState.customSwimspot.lat,
                                    homeState.customSwimspot.lon,
                                )
                            )
                        )
                        marker?.tag = homeState.customSwimspot
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetSwimspotContent(
    homeViewModel: HomeViewModel,
) {

    val homeState = homeViewModel.homeState.collectAsState().value

    // This box limits the size of the content
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {

            item {

                if (homeState.selectedSwimspot == null) {
                    Text(
                        text = "Utforsk badeplasser",
                        fontSize = 18.sp
                    )
                } else {

                    Text(
                        text = homeState.selectedSwimspot.spotName,
                        fontSize = 18.sp
                    )

                    homeViewModel.metAlertUiState.let { state ->
                        when (state) {
                            is MetAlertUiState.Success -> {

                                val koordinater = homeState.selectedSwimspot!!.getLatLng()

                                FarevarselCard(simpleMetAlertList = state.simpleMetAlertList.filter {
                                    it.isRelevantForCoordinate(koordinater)
                                })
                            }

                            is MetAlertUiState.Loading -> {
                                Text(text = "Loading")
                                Log.i("TestAlerts", "Loading")
                            }

                            is MetAlertUiState.Error -> {
                                Text(text = "Error")
                                Log.i("TestAlerts", "Error")
                            }
                        }
                    }

                    homeViewModel.locationForecastUiState.let { state ->
                        when (state) {
                            is LocationForecastUiState.Success -> {

                                WeatherCard(
                                    temperature = state.locationForecast.properties.timeseries[0].data.instant.details.air_temperature,
                                    windFromDirection = state.locationForecast.properties.timeseries[0].data.instant.details.windFromDirection,
                                    windSpeed = state.locationForecast.properties.timeseries[0].data.instant.details.wind_speed
                                )
                            }

                            is LocationForecastUiState.Loading -> {
                                Text(text = "Loading")
                            }

                            is LocationForecastUiState.Error -> {
                                Text(text = "Error")
                            }
                        }
                    }

                    homeViewModel.oceanForecastUiState.let { state ->
                        when (state) {
                            is OceanForecastState.Success -> {
                                WaterCard(
                                    temperature = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_water_temperature,
                                    waveHeight = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_surface_wave_height,
                                    waveToDirection = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_water_to_direction
                                )
                            }

                            is OceanForecastState.Loading -> {
                                Text(text = "Loading")
                            }

                            is OceanForecastState.Error -> {
                                Text(text = "Error")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FarevarselCard(
    simpleMetAlertList: List<SimpleMetAlert>
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Text(text = "${simpleMetAlertList.size} aktive farevarsler")
            if (simpleMetAlertList.size > 0) {
                simpleMetAlertList.forEach {
                    Text(
                        text = "- " + it.awarenessType.split(';')[1] + ", " + it.awarenessLevel.split(
                            ';'
                        )[1].replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun WeatherCard(
    temperature: Double,
    windFromDirection: Double,
    windSpeed: Double,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "Vær", fontWeight = FontWeight.ExtraBold)
            Text(text = "Temp: $temperature")
            Text(text = "Vind: $windSpeed m/s fra $windFromDirection")
        }

    }
}

@Composable
fun WaterCard(
    temperature: Double,
    waveHeight: Double,
    waveToDirection: Double,

    ) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Vann", fontWeight = FontWeight.ExtraBold)
            Text(text = "Temp: $temperature")
            Text(text = "Bølgehøyde: ${waveHeight}m fra $waveToDirection")
        }
    }
}


@Composable
fun loadMapStyleFromAssets(): String {
    val context = LocalContext.current
    return try {
        context.assets.open("map_style_dark.json").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}