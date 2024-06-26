package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.ktx.utils.sphericalDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.util.PreferencesManager
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationForecastRepository: LocationForecastRepositoryInterface,
    private val oceanForecastRepository: OceanForecastRepositoryInterface,
    private val metAlertRepository: MetAlertRepositoryInterface,
    private val swimspotRepository: SwimspotRepository,
    private val preferencesManager: PreferencesManager,
    application: Application
) : AndroidViewModel(application) {

    var locationForecastUiState: LocationForecastUiState by mutableStateOf(LocationForecastUiState.Loading)
    var oceanForecastUiState: OceanForecastState by mutableStateOf(OceanForecastState.Loading)

    private val _metAlertUiState = MutableStateFlow<MetAlertUiState>(MetAlertUiState.Loading)
    val metAlertUiState = _metAlertUiState.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    val locationData = MutableLiveData<Location?>()



    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.update { swimspotsState ->
                swimspotsState.copy(
                    allSwimspots = swimspotRepository.getAllSwimspots().first()
                )
            }
        }
        loadFarevarsler()
        preferencesManager.isOnboardingShown = true
        fetchLocation()

    }

    private fun fetchLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { location ->
                    locationData.postValue(location)
                }
            }
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplication<Application>().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            } else {
                locationData.postValue(null)
            }
        } catch (e: SecurityException) {
            locationData.postValue(null)
        }
    }

    fun onSwimspotPinClick(swimspot: Swimspot) {
        Log.i("SwimspotPinCLick registered", swimspot.spotName)
        updateSelectedSwimspot(swimspot)
        updateBottomSheetPosition(true)
        loadLocationForecast(swimspot.lat, swimspot.lon)
        loadOceanForecast(swimspot.lat, swimspot.lon)
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
        updateSearchbarActive(false)
        updateCustomSwimspot(customSwimspot)
        updateSelectedSwimspot(customSwimspot)
        updateBottomSheetPosition(true)
        loadLocationForecast(customSwimspot.lat, customSwimspot.lon)
        loadOceanForecast(customSwimspot.lat, customSwimspot.lon)
    }

    fun updateCustomMarker(marker: Marker?) {
        viewModelScope.launch(Dispatchers.Main) {
            homeState.value.customMarker?.remove()
            _homeState.update { homeState ->
                homeState.copy(customMarker = marker)
            }
        }
    }

    fun updateUserPositionMarker(marker: Marker?) {
        homeState.value.userPositionMarker?.remove()
        _homeState.update { homeState ->
            homeState.copy(userPositionMarker = marker)
        }
    }

    fun updateCameraPositionState(cameraPositionState: CameraPositionState) {
        _homeState.update { homeState ->
            homeState.copy(cameraPositionState = cameraPositionState)
        }
    }

    fun updateBottomSheetState(bottomSheetState: SheetState) {
        _homeState.update { homeState ->
            homeState.copy(bottomSheetState = bottomSheetState)
        }
    }

    private fun expandBottomSheet(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            if (homeState.value.bottomSheetPosition == BottomSheetPosition.Hidden) {
                updateBottomSheetPosition(true)
            }
            homeState.value.bottomSheetState?.expand()
        }
    }

    fun retractBottomSheet(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            homeState.value.bottomSheetState?.partialExpand()
        }
    }

    fun moveCamera(latLng: LatLng) {
        viewModelScope.launch {
            homeState.value.cameraPositionState?.animate(
                update = CameraUpdateFactory.newLatLng(
                    latLng,
                ),
                durationMs = 250
            )
        }
    }

    fun onFavouriteClick(
        swimspot: Swimspot
    ) {
        when (swimspot.favourited to swimspot.original) {
            false to true -> makeSwimspotFavourite(swimspot)
            true to true -> makeSwimspotNotFavourite(swimspot)
            false to false -> makeCustomSwimspotFavourite(swimspot)
            true to false -> makeCustomSwimspotNotFavourite(swimspot)
        }
    }

    private fun makeSwimspotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            // Remove marker
            removeMarker(swimspot)

            // Update database
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            // Add marker
            val icon =
                swimspot.getMarkerIcon(metAlertUiState.value) // Assuming this doesn't need to be on the main thread
            val latLng = swimspot.getLatLng()
            val markerData = MarkerData(latLng, icon, swimspot)
            addMarker(markerData)
        }
    }

    private fun makeSwimspotNotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            // Remove marker
            removeMarker(swimspot)

            // Update database
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            // Add marker
            val icon =
                swimspot.getMarkerIcon(metAlertUiState.value) // Assuming this doesn't need to be on the main thread
            val latLng = swimspot.getLatLng()
            val markerData = MarkerData(latLng, icon, swimspot)
            addMarker(markerData)
        }
    }

    private fun makeCustomSwimspotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            // Add to database and get new object with id
            swimspot.updateFavourite(!swimspot.favourited!!)
            swimspotRepository.upsertSwimspot(swimspot)

            val swimspotWithId = swimspotRepository.getLastAddedSwimspot()
            Log.i("I got here jjaa", swimspotWithId.id.toString())

            // Delete custom marker
            _homeState.update { homeState ->
                homeState.copy(customSwimspot = null)
            }
            withContext(Dispatchers.Main) {
                homeState.value.customMarker?.remove()
            }

            // Add marker
            _homeState.update { homeState ->
                homeState.copy(selectedSwimspot = swimspotWithId)
            }
            val icon =
                swimspot.getMarkerIcon(metAlertUiState.value) // Assuming this doesn't need to be on the main thread
            val latLng = swimspot.getLatLng()
            val markerData = MarkerData(latLng, icon, swimspot)
            addMarker(markerData)
        }
    }

    private fun makeCustomSwimspotNotFavourite(
        swimspot: Swimspot,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            // Remove marker
            removeMarker(swimspot)

            // Delete from database
            swimspotRepository.deleteSwimspot(swimspot)

            swimspot.updateFavourite(!swimspot.favourited!!)

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createAllMarkers(
        map: GoogleMap,
        center: LatLng
    ) {
        _homeState.update { homeState ->
            homeState.copy(map = map)
        }

        viewModelScope.launch(Dispatchers.Default) {
            homeState.value.allSwimspots
                .sortedBy { swimspot ->
                    swimspot.getLatLng().sphericalDistance(center)
                }
                .asFlow()
                .flatMapMerge(concurrency = 50) {
                    flowOf(it).onEach {
                        delay(20)  // Introduce a slight delay to stagger the markers
                    }.map { swimspot ->
                        val icon = swimspot.getMarkerIcon(metAlertUiState.value)
                        val latLng = swimspot.getLatLng()
                        MarkerData(latLng, icon, swimspot)
                    }
                }
                .collect { markerData ->
                    withContext(Dispatchers.Main) {
                        addMarker(markerData, map)
                    }
                }
        }
    }

    data class MarkerData(
        val latLng: LatLng,
        val icon: BitmapDescriptor,
        val swimspot: Swimspot
    )

    private fun addMarker(markerData: MarkerData, map: GoogleMap? = homeState.value.map) {
        map?.let { googleMap ->
            viewModelScope.launch(Dispatchers.Main) {
                val newMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(markerData.latLng)
                        .icon(markerData.icon)
                )

                newMarker?.tag = markerData.swimspot

                if (markerData.swimspot.id != null) {
                    _homeState.update { homeState ->
                        val updatedMarkers = homeState.allMarkers.toMutableMap()
                        updatedMarkers[markerData.swimspot.id]?.remove()
                        updatedMarkers[markerData.swimspot.id] = newMarker
                        homeState.copy(
                            allMarkers = updatedMarkers,
                            map = map
                        )
                    }
                }
            }
        }
    }

    private fun removeMarker(swimspot: Swimspot) {
        viewModelScope.launch(Dispatchers.Main) {

            homeState.value.allMarkers[swimspot.id]?.remove()
            val updatedMarkers = homeState.value.allMarkers.filter {
                it.key != swimspot.id
            }
            _homeState.update { homeState ->
                homeState.copy(allMarkers = updatedMarkers)

            }
        }

    }

    fun getSearchBarResults(queryString: String): List<Swimspot> {
        val searchInLocationString: Boolean = (queryString.length > 1)
        val resultList = homeState.value.allSwimspots.filter { swimspot ->
            swimspot.getQuerySearchString(searchInLocationString).contains(queryString, true)
        }.sortedBy { swimspot ->
            val searchString = swimspot.getQuerySearchString(searchInLocationString)
            searchString.indexOf(queryString, ignoreCase = true).takeIf { it >= 0 } ?: Int.MAX_VALUE
        }
        return resultList.take(10)
    }

    private fun updateSelectedSwimspot(swimspot: Swimspot) {
        _homeState.update { homeState ->
            homeState.copy(selectedSwimspot = swimspot)
        }
    }

    private fun updateCustomSwimspot(swimspot: Swimspot?) {
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

    private fun updateBottomSheetPosition(makeVisible: Boolean) { // Warning: Variable always true is kept for expandability
        val bottomSheetPosition: BottomSheetPosition = if (makeVisible) {
            BottomSheetPosition.Showing
        } else {
            BottomSheetPosition.Hidden
        }
        _homeState.update { homeState ->
            homeState.copy(bottomSheetPosition = bottomSheetPosition)
        }
    }

    fun onSearchBarSearch(
        searchQuery: String,
        coroutineScope: CoroutineScope
    ) {
        updateSearchbarActive(false)
        val resultList = getSearchBarResults(searchQuery)
        if (searchQuery != "") {
            if (resultList.isNotEmpty()) {
                onSearchBarSelectSwimspot(
                    swimspot = resultList[0],
                    coroutineScope = coroutineScope,
                )
                saveToSearchHistory(searchQuery)
            }
        }
    }

    fun getSearchHistory(): List<String> {
        return preferencesManager.getSearchHistory()
    }

    private fun saveToSearchHistory(searchQuery: String) {
        preferencesManager.addToSearchHistory(searchQuery.replace(";", ""))
    }

    fun onSearchHistoryClick(
        searchString: String,
    ) {
        updateSearchbarText(searchString)
    }

    fun onSearchBarSelectSwimspot(
        swimspot: Swimspot,
        coroutineScope: CoroutineScope
    ) {
        updateSearchbarText("")
        updateSearchbarActive(false)
        updateSelectedSwimspot(swimspot)
        moveCamera(swimspot.getLatLng())
        expandBottomSheet(coroutineScope = coroutineScope)
    }

    fun updateSearchbarText(searchBarText: String) {
        _homeState.update { homeState ->
            homeState.copy(searchBarText = searchBarText)
        }
    }

    fun updateSearchbarActive(
        searchBarActive: Boolean
    ) {
        _homeState.update { homeState ->
            homeState.copy(searchBarActive = searchBarActive)
        }
    }


    private fun loadLocationForecast(lat: Double, lon: Double) {
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

    private fun loadOceanForecast(lat: Double, lon: Double) {
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

    private fun loadFarevarsler() {
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