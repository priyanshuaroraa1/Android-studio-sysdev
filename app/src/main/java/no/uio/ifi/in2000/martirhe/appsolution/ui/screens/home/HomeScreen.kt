package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextWeek
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.WarningIconColor
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.HomeSearchBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.MediumHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.SmallHeader
import no.uio.ifi.in2000.martirhe.appsolution.util.UiEvent


@SuppressLint("PotentialBehaviorOverride")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState = homeViewModel.homeState.collectAsState().value
    val metAlertUiState = homeViewModel.metAlertUiState.collectAsState().value

    val cameraPositionState = rememberCameraPositionState {
        position = homeState.defaultCameraPosition
    }


    // TODO: Flytte dette til homeState?
    val mapStyleString = loadMapStyleFromAssets()
    val mapProperties = MapProperties(
        isMyLocationEnabled = false, mapStyleOptions = MapStyleOptions(mapStyleString)
    )

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


                if (homeState.selectedSwimspot != null) {
                    Text(
                        text = homeState.selectedSwimspot.spotName,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(top = dimensionResource(id = R.dimen.padding_medium))
                    )
                }
                Box(modifier = Modifier.align(Alignment.TopCenter)) {
                    DragHandle()
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }, modifier = Modifier.align(Alignment.TopEnd)
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
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeSearchBar(homeViewModel = homeViewModel)

            GoogleMap(
                modifier = Modifier, cameraPositionState = cameraPositionState, onMapClick = {
                    homeViewModel.onMapBackroundClick(it)
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.expand()
                        Log.i("Sheet", "expand")
                    }
                }, properties = mapProperties
            ) {
                MapEffect(
//                    key1 = homeState.customSwimspot
                    key1 = metAlertUiState
                ) { map ->

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


                    coroutineScope.launch {
                        val markersWithSwimspots = withContext(Dispatchers.Default) {
                            // Generate pairs of MarkerOptions and Swimspots
                            homeState.allSwimspots.map { swimspot ->
                                async {
                                    val icon = swimspot.getMarkerIcon(metAlertUiState)
                                    val options = MarkerOptions()
                                        .position(swimspot.getLatLng())
                                        .icon(icon)
                                    options to swimspot  // Return a pair of options and swimspot
                                }
                            }.awaitAll()
                        }

                        withContext(Dispatchers.Main) {
                            // Add markers to the map with tags
                            markersWithSwimspots.forEach { (options, swimspot) ->
                                map.addMarker(options)?.apply {
                                    tag = swimspot  // Set the swimspot as the tag for the marker
                                }
                            }
                        }
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
    if (homeState.showMetAlertDialog) {
        MetAlertDialog(homeViewModel = homeViewModel)
    }
    if (homeState.showWeatherInfoDialog) {
        WeatherInfoDialog(homeViewModel = homeViewModel)
    }
    if (homeState.showAccessibilityInfoDialog) {
        AccessibilityInfoDialog(homeViewModel)
    }
}

@Composable
fun DragHandle(
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    width: Dp = 50.dp,
    height: Dp = 5.dp,
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = width, height = height)
            .background(
                color = color,
                shape = RoundedCornerShape(50)
            )
    )
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
        LazyColumn {

            if (homeState.selectedSwimspot != null) {
                item {

                    Column(
                        modifier = Modifier.padding(horizontal = outerEdgePaddingValues)

                    ) {
                        val metAlertStateNew = homeViewModel.metAlertUiState.collectAsState().value
//                        val homeState = homeViewModel.homeState.collectAsState().value

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
                                    Text(text = "Loading")
                                }

                                is MetAlertUiState.Error -> {
                                    Text(text = "Error")
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
                                                    forecastNextHour =  locationForecastUiState.forecastNextHour,
                                                    oceanForecastRightNow =  oceanForecastState.oceanForecastRightNow,
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
                                Row {
                                    Spacer(modifier = Modifier.width(outerEdgePaddingValues))
                                    Text(text = "Loading")
                                }
                            }

                            is LocationForecastUiState.Error -> {
                                Row {
                                    Spacer(modifier = Modifier.width(outerEdgePaddingValues))
                                    Text(text = "Error")
                                }
                            }
                        }
                    }
                }

                item {
                    if (homeState.selectedSwimspot.accessibility != null) {
                        AccessibilityOptionsCard(
                            accessibilityStringList = homeState.selectedSwimspot.getAccecibilityStrings(),
                            homeViewModel = homeViewModel,
                            outerEdgePaddingValues = outerEdgePaddingValues)
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

@Composable
fun AccessibilityOptionsCard(
    accessibilityStringList: List<String>,
    homeViewModel: HomeViewModel,
    outerEdgePaddingValues: Dp,
) {
    Column(
        modifier = Modifier.padding(horizontal = outerEdgePaddingValues)

    ) {
        SmallHeader(text = stringResource(id = R.string.accessability_options_header))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                IconButton(
                    onClick = { homeViewModel.updateShowAccessibilityInfoDialog(true) },
                    modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = "Mer informasjon")
                }

                Column(
                    modifier = Modifier
                        .padding(all = dimensionResource(id = R.dimen.padding_medium))
                ) {
                    accessibilityStringList.forEachIndexed() { index, string ->
                        if (index != 0) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                        }
                        Row {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Punkt $index",
                                modifier = Modifier
                                    .padding(end = dimensionResource(id = R.dimen.padding_small)))
                            Text(text = string)

                        }

                    }
                }
            }
        }
    }
}


@Composable
fun SwimspotImage(
    swimspot: Swimspot,
) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))
    AsyncImage(
        model = swimspot.url,
        contentDescription = "Bilde av " + swimspot.spotName,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun WeatherNextWeekCard(
    outerEdgePaddingValues: Dp,
    forecastNextWeek: ForecastNextWeek,
    oceanForecastRightNow: OceanForecastRightNow?
) {

    Row {
        Spacer(modifier = Modifier.width(outerEdgePaddingValues))
        SmallHeader(text = "Neste 7 dager")
    }
    LazyRow {
        item {
            Card(
                modifier = Modifier.padding(horizontal = outerEdgePaddingValues),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                ) {

                    forecastNextWeek.weekList.forEach { forecastWeekday ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
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
                            if (oceanForecastRightNow != null) {
                                LargeAndSmallText(
                                    largeText = oceanForecastRightNow.getWaterTemperatureString() + "° ",
                                    smallText = "i vannet",
                                    smallerSize = true,
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
                            )
                        }
                        Spacer(
                            modifier = Modifier.width(40.dp)
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
    oceanForecastRightNow: OceanForecastRightNow? = null,
    homeViewModel: HomeViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
    ) {
        SmallHeader(
            text = "Den neste timen",
            paddingBottom = 0.dp,
            paddingTop = 0.dp
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { homeViewModel.updateShowWeatherInfoDialog(true) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Mer informasjon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.weight(1f))

        forecastNextHour.symbolCode?.let { WeatherIcon(it) }
        Spacer(modifier = Modifier.weight(0.5f))
        Column(horizontalAlignment = Alignment.Start) {
            LargeAndSmallText(
                largeText = "${forecastNextHour.getTemperatureString()}° ",
                smallText = "i lufta",
            )

            if (oceanForecastRightNow != null && oceanForecastRightNow.isSaltWater) {
                LargeAndSmallText(
                    largeText = oceanForecastRightNow.getWaterTemperatureString() + "° ",
                    smallText = "i vannet",
                )
            }


        }
        Spacer(modifier = Modifier.weight(1.5f))
    }
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
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
                    image = getFromDirectionPainterResource(forecastNextHour.getWindDirectionString()),
                    imageDescription = "Wind from " + forecastNextHour.getWindDirectionString(),
                )
                Text(
                    text = "Vind",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
            if (oceanForecastRightNow != null && oceanForecastRightNow.isSaltWater) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LargeAndSmallText(
                        largeText = oceanForecastRightNow.getWaveHeightString(),
                        smallText = "cm",
                        image = getFromDirectionPainterResource(oceanForecastRightNow.getWaveDirectionString()),
                        imageDescription = "Waves from " + oceanForecastRightNow.getWaveDirectionString(),
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
}

@Composable
fun MetAlertCard(
    simpleMetAlertList: List<SimpleMetAlert>,
    onClick: () -> Unit,
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
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
        ) {
            WarningIcon(
                warningIconColor, warningIconDescription.toString()
            )
            Text(
                text = "$numberOfAlerts aktive farevarsler",
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Se farevarsler",
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun MetAlertDialog(
    homeViewModel: HomeViewModel,
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    Dialog(
        onDismissRequest = { homeViewModel.updateShowMetAlertDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .fillMaxHeight(0.4f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

                )
        ) {

            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val headerString = if (homeState.selectedSwimspot?.spotName != "Plassert pin") {
                    "Farevarsler for ${homeState.selectedSwimspot?.spotName}"
                } else {
                    "Farevarsler for plassert pin"
                }
                MediumHeader(
                    text = headerString, paddingTop = 0.dp
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    homeState.metAlertDialogList.sortedByDescending { it.getAwarenesLevelInt() }
                        .takeIf { it.isNotEmpty() }?.let { sortedList ->
                            itemsIndexed(sortedList) { index, simpleMetAlert ->

                                if (index > 0) {
                                    Spacer(
                                        modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        WarningIcon(
                                            warningIconColor = simpleMetAlert.getAwarenessLevelColor(),
                                            warningIconDescription = simpleMetAlert.getAwarenessLevelDescription()
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                                        SmallHeader(
                                            text = simpleMetAlert.eventAwarenessName + ": " + simpleMetAlert.area,
                                            paddingTop = 0.dp,
                                            paddingBottom = 0.dp
                                        )

                                    }
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                                    Text(text = simpleMetAlert.description)
                                }
                            }
                        } ?: item {
                        Text(text = "Det er ingen aktive farevarsler nå.")
                    }
                }

                Button(
                    onClick = { homeViewModel.updateShowMetAlertDialog(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Lukk")
                }
            }
        }
    }
}

@Composable
fun WeatherInfoDialog(
    homeViewModel: HomeViewModel
) {
    Dialog(
        onDismissRequest = { homeViewModel.updateShowMetAlertDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .fillMaxHeight(0.4f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

                )
        ) {

            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.Start
            ) {

                MediumHeader(
                    text = stringResource(id = R.string.weather_info_dialog_header),
                    paddingTop = 0.dp,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item { 
                        Text(text = stringResource(id = R.string.weather_info_dialog_bodytext))
                    }
                }

                Button(
                    onClick = { homeViewModel.updateShowWeatherInfoDialog(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Lukk")
                }
            }
        }
    }
}

@Composable
fun AccessibilityInfoDialog(
    homeViewModel: HomeViewModel
) {
    Dialog(
        onDismissRequest = { homeViewModel.updateShowAccessibilityInfoDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .fillMaxHeight(0.4f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

                )
        ) {

            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.Start
            ) {

                MediumHeader(
                    text = stringResource(id = R.string.accessability_options_header),
                    paddingTop = 0.dp,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Text(text = stringResource(id = R.string.accessability_options_dialog_body))
                    }
                }

                Button(
                    onClick = { homeViewModel.updateShowAccessibilityInfoDialog(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Lukk")
                }
            }
        }
    }
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
            modifier = Modifier.padding(bottom = if (smallerSize) 0.dp else 3.dp)
        )
    }
}


@Composable
fun WarningIcon(
    warningIconColor: WarningIconColor,
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
    iconName: String, smallerSize: Boolean = false
) {
    Box {
        AsyncImage(
            model = "https://raw.githubusercontent.com/metno/weathericons/main/weather/png/$iconName.png",
            contentDescription = iconName,
            modifier = Modifier.size(if (smallerSize) 60.dp else 120.dp)
        )
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

@Composable
fun getFromDirectionPainterResource(windDirection: String): Painter {
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
        else -> R.drawable.north
    }
    return painterResource(id = resourceId)
}