package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextWeek
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.WarningIconColor
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.HomeSearchBar
import no.uio.ifi.in2000.martirhe.appsolution.util.UiEvent


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

        ) {

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

    val outerEdgePaddingValues: Dp = dimensionResource(id = R.dimen.padding_medium)

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

                        homeViewModel.metAlertUiState.let { state ->
                            when (state) {
                                is MetAlertUiState.Success -> {
                                    val coordinates = homeState.selectedSwimspot.getLatLng()
                                    val simpleMetAlertList = state.simpleMetAlertList.filter {
                                        it.isRelevantForCoordinate(coordinates)
                                    }

                                    MetAlertCard(
                                        simpleMetAlertList = simpleMetAlertList,
                                    )
                                }

                                is MetAlertUiState.Loading -> {
                                    Text(text = "Loading")
                                }

                                is MetAlertUiState.Error -> {
                                    Text(text = "Error")
                                }
                            }
                        }

                        homeViewModel.locationForecastUiState.let { state ->
                            when (state) {
                                is LocationForecastUiState.Success -> {
//                                    WeatherForecastCard(state.forecastNextHour)
                                    WeatherNextHourCard(
                                        state.forecastNextHour,
                                        state.locationForecast
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
                    }
                }
                item {


                    homeViewModel.locationForecastUiState.let { state ->
                        when (state) {
                            is LocationForecastUiState.Success -> {
//                                    WeatherForecastCard(state.forecastNextHour)
                                WeatherNextWeekCard(
                                    outerEdgePaddingValues = outerEdgePaddingValues,
                                    forecastNextWeek = state.forecastNextWeek,
                                )
                            }

                            is LocationForecastUiState.Loading -> {
                                Row {
                                    Spacer(modifier = Modifier.width(outerEdgePaddingValues))
                                    Text(text = "Loading")
                                }
                            }

                            is LocationForecastUiState.Error -> {
                                Row {
                                    Spacer(modifier = Modifier.width(outerEdgePaddingValues))
                                    Text(text = "Loading")
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
fun WeatherNextWeekCard(
    outerEdgePaddingValues: Dp,
    forecastNextWeek: ForecastNextWeek
) {

    Row {
        Spacer(modifier = Modifier.width(outerEdgePaddingValues))
        SmallHeader(text = "Neste 7 dager")
    }
    LazyRow() {
        item() {
            Card(
                modifier = Modifier
                    .padding(horizontal = outerEdgePaddingValues),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                ) {

                    forecastNextWeek.weekList.forEach { forecastWeekday ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.padding_medium))
                            )
                            Text(
                                text = forecastWeekday.getWeekdayString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            WeatherIcon(forecastWeekday.symbolCode, smallerSize = true)
                            LargeAndSmallText(
                                largeText = forecastWeekday.getTemperatureString() + "° ",
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


@Composable
fun WeatherNextHourCard(
    forecastNextHour: ForecastNextHour,
    locationForecast: LocationForecast
) {
    SmallHeader(
        text = "Den neste timen",
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Log.i(
            "Symbol code",
            locationForecast.properties.timeseries[0].data.instant.details.air_temperature.toString()
        )
        forecastNextHour.symbolCode?.let { WeatherIcon(it) }
        Spacer(modifier = Modifier.weight(0.5f))
        Column(horizontalAlignment = Alignment.Start) {
            LargeAndSmallText(
                largeText = "${forecastNextHour.getTemperatureString()}° ",
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
                    largeText = forecastNextHour.precipitationAmount.toString(),
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
                    largeText = forecastNextHour.getWindSpeedString(),
                    smallText = "m/s",
                    image = getWindDirectionPainterResource(forecastNextHour.getWindDirectionString()),
                    imageDescription = "Wind from " + forecastNextHour.getWindDirectionString(),
                )
                Log.i("Winddir", forecastNextHour.getWindDirectionString())
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
                    image = painterResource(id = R.drawable.north),
                    imageDescription = "Wind from " + forecastNextHour.getWindDirectionString(),
                )
                Text(
                    text = "Bølger",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun MetAlertCard(
    simpleMetAlertList: List<SimpleMetAlert>
) {
    val numberOfAlerts = simpleMetAlertList.size
    val warningIconColor = SimpleMetAlert.mostSevereColor(simpleMetAlertList)
    val warningIconDescription = SimpleMetAlert.mostSevereColor(simpleMetAlertList)

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
            WarningIcon(
                warningIconColor,
                warningIconDescription.toString()
            )
            Text(
                text = "$numberOfAlerts aktive farevarsler",
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
        image = painterResource(id = R.drawable.north),
        imageDescription = "Wind from north",
    )
}

@Composable
fun LargeAndSmallText(

    largeText: String,
    smallText: String,
    image: Painter? = null,
    imageDescription: String = "",
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    smallerSize: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (image != null) {

            Image(
                painter = image,
                contentDescription = imageDescription,
                modifier = Modifier.size(16.dp) // Set size or other modifiers as needed
            )
        }

        val largeStyle =
            if (smallerSize) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineSmall
        val smallStyle =
            if (smallerSize) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyLarge
        Text(
            text = largeText,
            color = color,
            style = largeStyle,
        )
        Text(
            text = smallText,
            color = color,
            style = smallStyle,
            modifier = Modifier
                .padding(bottom = if (smallerSize) 0.dp else 3.dp)
        )
    }
}


@Composable
fun WarningIcon(
    // TODO: Make colors an enum, not string?
    warningIconColor: WarningIconColor = WarningIconColor.GREEN,
    warningIconDescription: String,
) {
    val imageResource = when (warningIconColor) {
        WarningIconColor.YELLOW -> painterResource(id = R.drawable.warning_yellow)
        WarningIconColor.ORANGE -> painterResource(id = R.drawable.warning_orange)
        WarningIconColor.RED -> painterResource(id = R.drawable.warning_red)
        WarningIconColor.GREEN -> painterResource(id = R.drawable.warning_green)
    }
    Image(
        painter = imageResource,
        contentDescription = warningIconDescription,
        modifier = Modifier.size(24.dp) // Set size or other modifiers as needed
    )
}

@Composable
fun WeatherIcon(
    iconName: String,
    smallerSize: Boolean = false
) {
    Box {
        AsyncImage(
            model = "https://raw.githubusercontent.com/metno/weathericons/main/weather/png/$iconName.png",
            contentDescription = iconName,
            modifier = Modifier
                .size(if (smallerSize) 60.dp else 120.dp)
        )
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
fun loadMapStyleFromAssets(): String {
    val context = LocalContext.current
    return try {
        context.assets.open("map_style_light.json").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

@Composable
fun getWindDirectionPainterResource(windDirection: String): Painter {
    val resourceId = when (windDirection) {
        "north" -> R.drawable.north
        "north_northeast" -> R.drawable.north_northeast
        "northeast" -> R.drawable.northeast
        "east_northeast" -> R.drawable.east_northeast
        "east" -> R.drawable.east
        "east_southeast" -> R.drawable.east_southeast
        "southeast" -> R.drawable.southeast
        "south_southeast" -> R.drawable.south_southeast
        "south" -> R.drawable.south
        "south_southwest" -> R.drawable.south_southwest
        "southwest" -> R.drawable.southwest
        "west_southwest" -> R.drawable.west_southwest
        "west" -> R.drawable.west
        "west_northwest" -> R.drawable.west_northwest
        "northwest" -> R.drawable.northwest
        "north_northwest" -> R.drawable.north_northwest
        else -> R.drawable.north // Handle unexpected cases
    }
    return painterResource(id = resourceId)
}