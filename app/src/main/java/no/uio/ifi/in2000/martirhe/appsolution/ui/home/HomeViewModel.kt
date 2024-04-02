package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.content.Context
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepositoryInterface
import java.io.IOException


class HomeViewModel : ViewModel() {


    val locationForecastRepository: LocationForecastRepositoryInterface =
        LocationForecastRepository()
    var locationForecastUiState: LocationForecastUiState by mutableStateOf(LocationForecastUiState.Loading)

    val oceanForecastRepository: OceanForecastRepositoryInterface = OceanForecastRepository()
    var oceanForecastUiState: OceanForecastState by mutableStateOf(OceanForecastState.Loading)

    val metAlertRepository: MetAlertRepositoryInterface = MetAlertRepository()
    var metAlertUiState: MetAlertUiState by mutableStateOf(MetAlertUiState.Loading)

    // Dummy data
    val badeplasserDummy: List<Badeplass> = listOf(
        Badeplass("00", "Huk", 59.895002996529485, 10.67554858599053),
        Badeplass("01", "Katten", 59.85526122555474, 10.783288859002855),
        Badeplass("02", "Hovedøya", 59.89531481816671, 10.724690847364073),
        Badeplass("03", "Tjuvholmen", 59.9064275008578, 10.721101654359384),
        Badeplass("04", "Testbadeplass", 62.2631564, 5.2425385),
    )
    val badeplasser = badeplasserDummy
    var customBadeplass by mutableStateOf<Badeplass>(
        Badeplass(
            "",
            "Valgt sted",
            59.895002996529485,
            10.67554858599053
        )
    )


    var homeScreenUiState: HomeScreenUiState by mutableStateOf(
        HomeScreenUiState(
            lastKnownLocation = null,
            swimSpots = badeplasserDummy,
            customSwimSpot = null,
            selectedSwimSpot = null,
            bottomSheetPosition = BottomSheetPosition.Hidden
        )
    )


    // Variables for Map
    var selectedBadeplass by mutableStateOf<Badeplass>(badeplasser[0])
    var showBottomSheet by mutableStateOf(false)
    var isCustomBadeplass by mutableStateOf(false)
    var showBadeplassCard by mutableStateOf(false)
    var showCustomMarker by mutableStateOf(false)
    var customMarkerLocation by mutableStateOf<LatLng>(LatLng(59.911491, 10.757933))


    fun showBottomSheet() {
        homeScreenUiState.bottomSheetPosition = BottomSheetPosition.Showing
    }


    fun onBadeplassPinClick(
        badeplass: Badeplass
    ) {
//        showBottomSheet() TODO
        selectedBadeplass = badeplass
        showCustomMarker = false
        showBottomSheet = true
        isCustomBadeplass = false
        loadLocationForecast(badeplass.lat, badeplass.lon)
        loadOceanForecast(badeplass.lat, badeplass.lon)
        loadFarevarsler()

    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun onMapBackroundClick(
        latLng: LatLng,
        coroutineScope: CoroutineScope,
        cameraPositionState: CameraPositionState,
        scaffoldState: BottomSheetScaffoldState
    ) {
        if (showBadeplassCard) {
            showBadeplassCard = false
//            showCustomMarker = false
//            showBottomSheet() TODO
            coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() }
        } else {
//            showBottomSheet() TODO
            coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
            customMarkerLocation = latLng
            customBadeplass.lat = latLng.latitude
            customBadeplass.lon = latLng.longitude
            loadLocationForecast(customBadeplass.lat, customBadeplass.lon)
            loadOceanForecast(customBadeplass.lat, customBadeplass.lon)
            selectedBadeplass = customBadeplass
            showCustomMarker = true
            showBadeplassCard = true


            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(latLng, 11f),
                    ),
                    durationMs = 300
                )
            }

        }
    }

    // Variables for SearchBar
    var searchBarText by mutableStateOf("")
    var searchBarActive by mutableStateOf(false)
    var searchBarHistory = mutableStateListOf<String>()

    fun updateSearchBarHistory() {

        if (searchBarText.trim().isNotEmpty()) {
            if (searchBarText in searchBarHistory) {
                searchBarHistory.remove(searchBarText.trim())
            }
            searchBarHistory.add(0, searchBarText.trim())
        }

    }

    fun onSearch() {
        updateSearchBarHistory()
        if (badeplasser.find { it.navn.lowercase() == searchBarText.lowercase() } != null) {

            onBadeplassPinClick(badeplasser.find { it.navn.lowercase() == searchBarText.lowercase() }!!)

        }
        searchBarText = ""
        searchBarActive = false

    }



    fun loadLocationForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationForecastUiState = LocationForecastUiState.Loading
            locationForecastUiState = try {
                LocationForecastUiState.Success(
                    locationForecastRepository.getLocationForecast(
                        lat,
                        lon
                    )
                )
            } catch (e: IOException) {
                LocationForecastUiState.Error
            } catch (e: ResponseException) {
                LocationForecastUiState.Error
            }
        }
    }

    fun loadOceanForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            oceanForecastUiState = OceanForecastState.Loading
            oceanForecastUiState = try {
                OceanForecastState.Success(oceanForecastRepository.getOceanForecast(lat, lon))
            } catch (e: IOException) {
                OceanForecastState.Error
            } catch (e: ResponseException) {
                OceanForecastState.Error
            }
        }
    }

    fun loadFarevarsler() {
        viewModelScope.launch(Dispatchers.IO) {
            metAlertUiState = MetAlertUiState.Loading
            metAlertUiState = try {
                MetAlertUiState.Success(
                    metAlertRepository.getMetAlerts(),
                    metAlertRepository.getSimpleMetAlerts()
                )
            } catch (e: IOException) {
                MetAlertUiState.Error
            } catch (e: ResponseException) {
                MetAlertUiState.Error
            }
        }
    }
}