package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.BottomSheetSwimspotContent
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.DragHandle
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.FavoriteIcon
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.HomeSearchBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.dialogs.AccessibilityInfoDialog
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.dialogs.MetAlertDialog
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.dialogs.WeatherInfoDialog
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.loadMapStyleFromAssets


@SuppressLint("PotentialBehaviorOverride")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    swimspotId: Int = -1,
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    var initialPosition = homeState.defaultCameraPosition
    val navigateToSwimspot = homeState.allSwimspots.find { it.id == swimspotId }

    if (navigateToSwimspot != null) {
        initialPosition = CameraPosition.fromLatLngZoom(
            LatLng(navigateToSwimspot.lat, navigateToSwimspot.lon), 11f
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = initialPosition
    }
    homeViewModel.updateCameraPositionState(cameraPositionState)
    val locationData by homeViewModel.locationData.observeAsState()


    homeViewModel.updateCameraPositionState(cameraPositionState)

    val mapStyleString = loadMapStyleFromAssets()
    val mapProperties = MapProperties(
        isMyLocationEnabled = false,
        mapStyleOptions = MapStyleOptions(mapStyleString),
        isBuildingEnabled = false,
    )
    val mapUiSettings = MapUiSettings(
        compassEnabled = false,
        indoorLevelPickerEnabled = false,
        mapToolbarEnabled = false,
        myLocationButtonEnabled = false,
        rotationGesturesEnabled = false,
        scrollGesturesEnabled = true,
        scrollGesturesEnabledDuringRotateOrZoom = true,
        tiltGesturesEnabled = false,
        zoomControlsEnabled = false,
        zoomGesturesEnabled = true,
    )

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    homeViewModel.updateBottomSheetState(scaffoldState.bottomSheetState)


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
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart),
                        verticalAlignment = Alignment.Top
                    ) {

                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            Text(
                                text = homeState.selectedSwimspot.spotName,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier
                                    .padding(
                                        top = dimensionResource(id = R.dimen.padding_medium),
                                    )
                                    .weight(1f)
                                    .clickable {
                                        coroutineScope.launch {
                                            val currentZoom = cameraPositionState.position.zoom
                                            val zoomIncrement = 1
                                            cameraPositionState.animate(
                                                update = CameraUpdateFactory.newLatLngZoom(
                                                    homeState.selectedSwimspot.getLatLng(),
                                                    currentZoom + zoomIncrement
                                                ),
                                                durationMs = 250
                                            )
                                        }
                                    }
                            )
                        }

                        IconButton(
                            onClick = {
                                homeViewModel.onFavouriteClick(homeState.selectedSwimspot)
                            },
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                        ) {
                            FavoriteIcon(
                                homeState = homeState
                            )
                        }
                    }
                }
                Box(modifier = Modifier.align(Alignment.TopCenter)) {
                    DragHandle()
                }
            }
        },
        sheetPeekHeight = homeState.bottomSheetPosition.heightDp,

        ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            GoogleMap(
                modifier = Modifier,
                cameraPositionState = cameraPositionState,
                uiSettings = mapUiSettings,
                onMapClick = {
                    homeViewModel.onMapBackroundClick(it)
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.expand()
                        Log.i("Sheet", "expand")
                    }
                },
                properties = mapProperties,
                onMapLoaded = {

                }
            ) {
                MapEffect {
                    if (swimspotId != -1) {
                        homeState.allSwimspots.find { it.id == swimspotId }
                            ?.let {
                                homeViewModel.onSwimspotPinClick(it)
                                homeViewModel.moveCamera(it.getLatLng())
                            }
                    }
                }
                MapEffect { map ->
                    Log.i("Map effect called", "Map effect 1 called")
                    map.setOnMarkerClickListener { marker ->

                        val swimspot = marker.tag as? Swimspot
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
                        if (swimspot != null) {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }

                        true
                    }
                }
                MapEffect{ map ->
                    map.setOnMapLoadedCallback {
                        coroutineScope.launch(Dispatchers.Default) {
                            homeViewModel.createAllMarkers(
                                map,
                                cameraPositionState.position.target
                            )

                        }
                    }
                }

                MapEffect(key1 = locationData) {map ->
                    if (locationData != null) {
                        homeState.userPositionMarker?.remove()
                        val newMarker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(locationData!!.latitude, locationData!!.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.userpos)
                        )


                        )
                        homeViewModel.updateUserPositionMarker(newMarker)
                    }
                }

                MapEffect(key1 = homeState.customSwimspot) { map ->
                    Log.i("Map effect called", "Map effect 3 called")

                    if (homeState.customSwimspot != null) {
                        val newMarker = map.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        homeState.customSwimspot.lat,
                                        homeState.customSwimspot.lon,
                                    )
                                )
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_38))
                        )
                        newMarker?.tag = homeState.customSwimspot
                        homeViewModel.updateCustomMarker(newMarker)
                    }
                }
            }

            HomeSearchBar(
                homeViewModel = homeViewModel,
                onQueryChange = { homeViewModel.updateSearchbarText(it) },
                onSearch = {
                    homeViewModel.onSearchBarSearch(it, coroutineScope)
                },
                onActiveChange = {
                    homeViewModel.updateSearchbarActive(it)
                    if (it) {
                        homeViewModel.retractBottomSheet(coroutineScope)
                    }
                }
            )
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




























