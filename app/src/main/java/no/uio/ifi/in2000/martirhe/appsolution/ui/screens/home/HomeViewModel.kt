package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationForecastRepository: LocationForecastRepositoryInterface,
    private val oceanForecastRepository: OceanForecastRepositoryInterface,
    private val metAlertRepository: MetAlertRepositoryInterface,
    private val swimspotRepository: SwimspotRepository,
) : ViewModel() {

    var locationForecastUiState: LocationForecastUiState by mutableStateOf(LocationForecastUiState.Loading)
    var oceanForecastUiState: OceanForecastState by mutableStateOf(OceanForecastState.Loading)
//    var metAlertUiState: MetAlertUiState by mutableStateOf(MetAlertUiState.Loading)

    private val _metAlertUiState = MutableStateFlow<MetAlertUiState>(MetAlertUiState.Loading)
    val metAlertUiState = _metAlertUiState.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.update { swimspotsState ->
                swimspotsState.copy(
                    allSwimspots = swimspotRepository.getAllSwimspots().first()
                )
            }
        }
        loadFarevarsler()
    }

    fun onSwimspotPinClick(swimspot: Swimspot) {
        updateSelectedSwimspot(swimspot)
        updateBottomSheetPosition(true)
        loadLocationForecast(swimspot.lat, swimspot.lon)
        loadOceanForecast(swimspot.lat, swimspot.lon)
//        loadFarevarsler()
        // TODO: Er det noe mer som skal gjøres her?
    }

    fun onMapBackroundClick(
        latLng: LatLng,
    ) {
        val customSwimspot = Swimspot(
            id = null,
            googleId = null,
            spotName = "Plassert pin",
            lat = latLng.latitude,
            lon = latLng.longitude,
            accessibility = null,
            locationstring = null,
            original = false,
            favourited = false,
            url = null,
        )

        updateCustomSwimspot(customSwimspot)
        updateSelectedSwimspot(customSwimspot)
        updateBottomSheetPosition(true)
        loadLocationForecast(customSwimspot.lat, customSwimspot.lon)
        loadOceanForecast(customSwimspot.lat, customSwimspot.lon)
//        loadFarevarsler()

    }

    fun updateCustomMarker(marker: Marker?) {
        homeState.value.customMarker?.remove()
        _homeState.update { homeState ->
            homeState.copy(customMarker = marker)
        }
    }

    fun onFavouriteClick(
        swimspot: Swimspot
    ) {
        Log.i("GOT HERE", swimspot.favourited.toString() + ", " + swimspot.original.toString())
        when (swimspot.favourited to swimspot.original) {
            false to true -> makeSwimspotFavourite(swimspot)
            true to true -> makeSwimspotNotFavourite(swimspot)
            false to false -> makeCustomSwimspotFavourite(swimspot)
            true to false -> makeCustomSwimspotNotFavourite(swimspot)
        }
    }

    fun makeSwimspotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch {
            // Remove marker
            removeMarker(swimspot)

            // Update database
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            // Add marker
            addMarker(swimspot)
        }
    }

    fun makeSwimspotNotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch {
            // Remove marker
            removeMarker(swimspot)

            // Update database
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            // Add marker
            addMarker(swimspot)
        }
    }

    fun makeCustomSwimspotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch {
            // Add to database and get new object with id
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            val swimspotWithId = swimspotRepository.getLastAddedSwimspot()
            Log.i("I got here jjaa", swimspotWithId.id.toString())

            // Delete custom marker
            _homeState.update { homeState ->
                homeState.copy(customSwimspot = null)
            }
            Log.i("I got here jjaa", swimspotWithId.id.toString())
            homeState.value.customMarker?.remove()

            // Add marker
            _homeState.update { homeState ->
                homeState.copy(selectedSwimspot = swimspotWithId)
            }
            addMarker(swimspotWithId)
        }
    }

    fun makeCustomSwimspotNotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch {
            // Remove marker
            removeMarker(swimspot)

            // Delete from database
            swimspotRepository.deleteSwimspot(swimspot)

            swimspot.updateFavourite(!swimspot.favourited!!)

        }
    }


    fun createAllMarkers(
        map: GoogleMap,
    ) {
        _homeState.update { homeState ->
            homeState.copy(map = map)
        }
        homeState.value.allSwimspots.forEach {
            addMarker(it)
        }
    }

    fun removeMarker(swimspot: Swimspot) {
        viewModelScope.launch {
            // Makeing sure adding/removing Markers happens on the Main thread
            withContext(Dispatchers.Main) {
                homeState.value.allMarkers[swimspot.id]?.remove()
                val updatedMarkers = homeState.value.allMarkers.filter {
                    it.key != swimspot.id
                }
                _homeState.update { homeState ->
                    homeState.copy(allMarkers = updatedMarkers)
                }
            }
        }

    }

    fun addMarker(
        swimspot: Swimspot,
        map: GoogleMap? = homeState.value.map,
    ) {
        viewModelScope.launch {

            // Makeing sure adding/removing Markers happens on the Main thread
            withContext(Dispatchers.Main) {

                if (map != null) {
                    val icon = swimspot.getMarkerIcon(metAlertUiState.value)
                    val newMarker = map.addMarker(
                        MarkerOptions()
                            .position(swimspot.getLatLng())
                            .icon(icon)
                    )

                    newMarker?.tag = swimspot

                    _homeState.update { homeState ->
                        val updatedMarkers = homeState.allMarkers.toMutableMap()
                        updatedMarkers[swimspot.id]?.remove()
                        updatedMarkers[swimspot.id!!] = newMarker
                        homeState.copy(
                            allMarkers = updatedMarkers,
                            map = map
                        )
                    }
                }
            }
        }
    }

//    fun updateMarker(
//        swimspot: Swimspot,
//        metAlertUiState: MetAlertUiState
//    ) {
//        viewModelScope.launch {
//            val marker = homeState.value.allMarkers[swimspot.id]
//            val map = homeState.value.map
//            if (marker != null) {
//                marker.remove()
//                _homeState.update { homeState ->
//                    val updatedMarkers = homeState.allMarkers.toMutableMap()
//                    updatedMarkers.remove(swimspot.id)
//                    homeState.copy(
//                        allMarkers = updatedMarkers,
//                        map = map
//                    )
//                }
//                Log.i("Old marker removed:", swimspot.id.toString())
//                val icon = swimspot.getMarkerIcon(metAlertUiState)
//                val newMarker = map?.addMarker(
//                    MarkerOptions()
//                        .position(swimspot.getLatLng())
//                        .icon(icon)
//                )
//                if (newMarker != null) {
//                    newMarker.tag = swimspot
//                }
//                _homeState.update { homeState ->
//                    val updatedMarkers = homeState.allMarkers.toMutableMap()
//                    updatedMarkers[swimspot.id ?: 0] = marker
//                    homeState.copy(
//                        allMarkers = updatedMarkers,
//                        map = map
//                    )
//                }
//            }
//        }
//    }

    fun updateSelectedSwimspot(swimspot: Swimspot) {
        _homeState.update { homeState ->
            homeState.copy(selectedSwimspot = swimspot)
        }
    }

    fun updateCustomSwimspot(swimspot: Swimspot?) {
        _homeState.update { homeState ->
            homeState.copy(customSwimspot = swimspot)
        }
    }

    fun updateShowMetAlertDialog(showMetAlertDialog: Boolean) {
        _homeState.update { homeState ->
            homeState.copy(showMetAlertDialog = showMetAlertDialog)
        }
    }

    fun updateShowWeatherInfoDialog(showWeatherInfoDialog: Boolean) {
        _homeState.update { homeState ->
            homeState.copy(showWeatherInfoDialog = showWeatherInfoDialog)
        }
    }

    fun updateShowAccessibilityInfoDialog(showAccessibilityInfoDialog: Boolean) {
        _homeState.update { homeState ->
            homeState.copy(showAccessibilityInfoDialog = showAccessibilityInfoDialog)
        }
    }

    fun updateMetAlertDialogList(metAlertDialogList: List<SimpleMetAlert>) {
        _homeState.update { homeState ->
            homeState.copy(metAlertDialogList = metAlertDialogList)
        }
    }

    // TODO: Change name/remove yeah
    fun updateBottomSheetPosition(makeVisible: Boolean) {
        val bottomSheetPosition: BottomSheetPosition
        bottomSheetPosition = if (makeVisible) {
            BottomSheetPosition.Showing
        } else {
            BottomSheetPosition.Hidden
        }
        _homeState.update { homeState ->
            homeState.copy(bottomSheetPosition = bottomSheetPosition)
        }
    }

    fun onSearchBarSearch() {
        // Todo: Implementere denne
    }

    fun updateSearchbarText(searchBarText: String) {
        _homeState.update { homeState ->
            homeState.copy(searchBarText = searchBarText)
        }
    }

    fun updateSearchbarActive(searchBarActive: Boolean) {
        _homeState.update { homeState ->
            homeState.copy(searchBarActive = searchBarActive)
        }
    }


    fun loadLocationForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationForecastUiState = LocationForecastUiState.Loading
            locationForecastUiState = try {
                LocationForecastUiState.Success(
                    locationForecastRepository.getLocationForecast(lat, lon),
                    locationForecastRepository.getForecastNextHour(lat, lon),
                    locationForecastRepository.getForecastNextWeek(lat, lon),
                )
            } catch (e: IOException) {
                LocationForecastUiState.Error
            } catch (e: ResponseException) {
                LocationForecastUiState.Error
            } catch (e: UnresolvedAddressException) {
                LocationForecastUiState.Error
            } catch (e: Error) {
                LocationForecastUiState.Error
            }
        }
    }

    fun loadOceanForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            oceanForecastUiState = OceanForecastState.Loading
            oceanForecastUiState = try {
                val oceanForecast = oceanForecastRepository.getOceanForecast(lat, lon)
                OceanForecastState.Success(
                    oceanForecastRepository.getOceanForecastRightNow(oceanForecast, lat, lon)
                )
            } catch (e: IOException) {
                OceanForecastState.Error
            } catch (e: ResponseException) {
                OceanForecastState.Error
            } catch (e: UnresolvedAddressException) {
                OceanForecastState.Error
            } catch (e: Exception) {
                OceanForecastState.Error
            } catch (e: Error) {
                OceanForecastState.Error
            }
        }
    }

    fun loadFarevarsler() {
        viewModelScope.launch(Dispatchers.IO) {
            _metAlertUiState.update {
                MetAlertUiState.Loading
            }
            _metAlertUiState.update {
                try {
                    MetAlertUiState.Success(
                        metAlertRepository.getMetAlerts(),
                        metAlertRepository.getSimpleMetAlerts()
                    )
                } catch (e: IOException) {
                    MetAlertUiState.Error
                } catch (e: ResponseException) {
                    MetAlertUiState.Error
                } catch (e: UnresolvedAddressException) {
                    MetAlertUiState.Error
                } catch (e: Error) {
                    MetAlertUiState.Error
                }
            }
        }
    }
}