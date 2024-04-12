package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
import no.uio.ifi.in2000.martirhe.appsolution.R
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
        sheetContainerColor = MaterialTheme.colorScheme.background,
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
        sheetPeekHeight = homeState.bottomSheetPosition.heightDp,

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
    outerEdgePaddingValues: Dp = dimensionResource(id = R.dimen.padding_medium)
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        LazyColumn() {

            if (homeState.selectedSwimspot != null) {
                item {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = outerEdgePaddingValues)

                    ) {
                        Text(
                            text = homeState.selectedSwimspot.spotName,
                            style = MaterialTheme.typography.headlineMedium,
                        )

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                                    .padding(start = dimensionResource(id = R.dimen.padding_medium))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = "Warning",
                                    tint = Color.Red,
                                )
                                Text(
                                    text = "3 aktive farevarsler",
                                    modifier = Modifier
                                        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Se farevarsler",
                                    modifier = Modifier
                                        .padding(end = dimensionResource(id = R.dimen.padding_medium))
                                )
                            }

                        }

                        SmallHeader(
                            text = "Den neste timen",
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            WeatherIcon()
                            Spacer(modifier = Modifier.weight(0.5f))

                            Column(horizontalAlignment = Alignment.Start) {
                                LargeAndSmallText(
                                    largeText = "19° ",
                                    smallText = "i lufta",
                                )
                                LargeAndSmallText(
                                    largeText = "11° ",
                                    smallText = "i vannet",
                                )
                            }
                            Spacer(modifier = Modifier.weight(1.5f))
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LargeAndSmallText(
                                        largeText = "4",
                                        smallText = "mm",
                                    )
                                    Text(
                                        text = "Nedbør",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LargeAndSmallText(
                                        largeText = "6",
                                        smallText = "m/s",
                                        icon = Icons.Default.ArrowDownward,
                                        iconDescription = "Fra nord"
                                    )
                                    Text(
                                        text = "Vind",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )

                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LargeAndSmallText(
                                        largeText = "40",
                                        smallText = "cm",
                                        icon = Icons.Default.ArrowDownward,
                                        iconDescription = "Fra nord"
                                    )
                                    Text(
                                        text = "Bølger",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }

                        SmallHeader(text = "Neste 7 dager")

                    }
                }
                item {


                    LazyRow() {
                        item() {
                            Card(
                                modifier = Modifier
//                                    .width(500.dp)
//                                    .height(100.dp)
                                    .padding(horizontal = outerEdgePaddingValues),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                ) {

                                    for (i in 1..7) {

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Spacer(
                                                modifier = Modifier
                                                    .height(dimensionResource(id = R.dimen.padding_medium))
                                            )
                                            Text(
                                                text = "Ukedag",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            WeatherIcon("fair_day", smallerSize = true)
                                            LargeAndSmallText(
                                                largeText = "19° ",
                                                smallText = "i lufta",
                                                smallerSize = true,
                                            )
                                            LargeAndSmallText(
                                                largeText = "12° ",
                                                smallText = "i vannet",
                                                smallerSize = true,
                                            )
                                            Spacer(
                                                modifier = Modifier
                                                    .height(dimensionResource(id = R.dimen.padding_medium))
                                            )


                                        }
                                        if (i < 7) {
                                            Spacer(
                                                modifier = Modifier
                                                    .width(40.dp)
                                            )
                                        }
                                    }

                                }
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_large))
                    )

                }
            }
        }
    }
}


@Composable
fun SmallHeader(
    text: String,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.padding_large),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLargeAndSmallText() {
    LargeAndSmallText(
        largeText = "19",
        smallText = "mm",
        icon = Icons.Default.ArrowDownward,
        iconDescription = "Fra nord"
    )
}

@Composable
fun LargeAndSmallText(

    largeText: String,
    smallText: String,
    icon: ImageVector? = null,
    iconDescription: String = "",
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    smallerSize: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                tint = color,
                contentDescription = iconDescription,
                modifier = Modifier
                    .padding(bottom = 2.dp),
            )
        }
        val largeStyle =
            if (smallerSize) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineSmall
        val smallStyle =
            if (smallerSize) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyLarge
        Text(
            text = largeText,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = largeStyle,
        )
        Text(
            text = smallText,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = smallStyle,
            modifier = Modifier
                .padding(bottom = if (smallerSize) 0.dp else 3.dp)
        )
    }
}


@Composable
fun WeatherIcon(
    iconName: String = "fair_day",
    smallerSize: Boolean = false
) {
    Box {
        AsyncImage(
            model = "https://raw.githubusercontent.com/metno/weathericons/main/weather/png/$iconName.png",
            contentDescription = iconName,
            modifier = Modifier
                .size(if (smallerSize) 60.dp else 120.dp)
        )
//        Image(
//            painter = painterResource(id = R.drawable.fair_day),
//            contentDescription = "Sol",
//            alignment = Alignment.Center,
//            modifier = Modifier
//                .size(if (smallerSize) 60.dp else 100.dp)
//        )
    }

}


//    // This box limits the size of the content
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(280.dp)
//    ) {
//
//        LazyColumn(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//        ) {
//
//            item {
//
//                if (homeState.selectedSwimspot == null) {
//                    Text(
//                        text = "Utforsk badeplasser",
//                        fontSize = 18.sp
//                    )
//                } else {
//
//                    Text(
//                        text = homeState.selectedSwimspot.spotName,
//                        fontSize = 18.sp
//                    )
//
//                    homeViewModel.metAlertUiState.let { state ->
//                        when (state) {
//                            is MetAlertUiState.Success -> {
//
//                                val koordinater = homeState.selectedSwimspot!!.getLatLng()
//
//                                FarevarselCard(simpleMetAlertList = state.simpleMetAlertList.filter {
//                                    it.isRelevantForCoordinate(koordinater)
//                                })
//                            }
//
//                            is MetAlertUiState.Loading -> {
//                                Text(text = "Loading")
//                                Log.i("TestAlerts", "Loading")
//                            }
//
//                            is MetAlertUiState.Error -> {
//                                Text(text = "Error")
//                                Log.i("TestAlerts", "Error")
//                            }
//                        }
//                    }
//
//                    homeViewModel.locationForecastUiState.let { state ->
//                        when (state) {
//                            is LocationForecastUiState.Success -> {
//
//                                WeatherCard(
//                                    temperature = state.locationForecast.properties.timeseries[0].data.instant.details.air_temperature,
//                                    windFromDirection = state.locationForecast.properties.timeseries[0].data.instant.details.windFromDirection,
//                                    windSpeed = state.locationForecast.properties.timeseries[0].data.instant.details.wind_speed
//                                )
//                            }
//
//                            is LocationForecastUiState.Loading -> {
//                                Text(text = "Loading")
//                            }
//
//                            is LocationForecastUiState.Error -> {
//                                Text(text = "Error")
//                            }
//                        }
//                    }
//
//                    homeViewModel.oceanForecastUiState.let { state ->
//                        when (state) {
//                            is OceanForecastState.Success -> {
//                                WaterCard(
//                                    temperature = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_water_temperature,
//                                    waveHeight = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_surface_wave_height,
//                                    waveToDirection = state.oceanForecast.properties.timeseries[0].data.instant.details.sea_water_to_direction
//                                )
//                            }
//
//                            is OceanForecastState.Loading -> {
//                                Text(text = "Loading")
//                            }
//
//                            is OceanForecastState.Error -> {
//                                Text(text = "Error")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


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
        context.assets.open("map_style_light.json").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}