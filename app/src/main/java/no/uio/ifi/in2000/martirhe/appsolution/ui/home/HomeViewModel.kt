package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import java.nio.channels.UnresolvedAddressException
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.SimpleMetAlert


sealed interface LocationForecastUiState {
    data class Success(
        val locationForecast: LocationForecast
    ): LocationForecastUiState
    object Loading: LocationForecastUiState
    object Error: LocationForecastUiState
}

sealed interface OceanForecastUiState {
    data class Success(
        val oceanForecast: OceanForecast
    ): OceanForecastUiState
    object Loading: OceanForecastUiState
    object Error: OceanForecastUiState
}

sealed interface FarevarselUiState {
    data class Success(
        val farevarselCollection: FarevarselCollection,
        val simpleMetAlertList: List<SimpleMetAlert>
    ): FarevarselUiState
    object Loading: FarevarselUiState
    object Error: FarevarselUiState
}

data class PocLocationForecastUiState(
    val locationForecastUiState: LocationForecastUiState = LocationForecastUiState.Loading
)
data class PocOceanForecastUiState(
    val oceanForecastUiState: OceanForecastUiState = OceanForecastUiState.Loading
)

data class PocFarevarselUiState(
    val farevarselUiState: FarevarselUiState = FarevarselUiState.Loading
)


enum class BottomSheetHeightState(val heightDp: Dp) {
    Hidden(0.dp),
    Showing(125.dp)
} // TODO: Flytte denne og de andre greiene over til egne filer



class HomeViewModel : ViewModel() {

    val locationForecastRepository: LocationForecastRepositoryInterface = LocationForecastRepository()
    var locationForecastUiState = MutableStateFlow(PocLocationForecastUiState())

    val oceanForecastRepository: OceanForecastRepositoryInterface = OceanForecastRepository()
    var oceanForecastUiState = MutableStateFlow(PocOceanForecastUiState())

    val farevarselRepository: FarevarselRepositoryInterface = FarevarselRepository()
    var farevarselUiState = MutableStateFlow(PocFarevarselUiState())

    // Dummy data
    val badeplasserDummy: List<Badeplass> = listOf(
        Badeplass("00", "Huk", 59.895002996529485, 10.67554858599053),
        Badeplass("01", "Katten", 59.85526122555474, 10.783288859002855),
        Badeplass("02", "Hovedøya", 59.89531481816671, 10.724690847364073),
        Badeplass("03", "Tjuvholmen", 59.9064275008578, 10.721101654359384),
        Badeplass("04", "Testbadeplass", 62.2631564, 5.2425385),
    )
    val badeplasser = badeplasserDummy
    var customBadeplass by mutableStateOf<Badeplass>(Badeplass("", "Valgt sted", 59.895002996529485, 10.67554858599053))




    // Variables for Map
    var selectedBadeplass by mutableStateOf<Badeplass>(badeplasser[0])
    var showBottomSheet by mutableStateOf(false)
    var isCustomBadeplass by mutableStateOf(false)
    var showBadeplassCard by mutableStateOf(false)
    var showCustomMarker by mutableStateOf(false)
    var customMarkerLocation by mutableStateOf<LatLng>(LatLng(59.911491, 10.757933))



    // BottomSheet LiveData
    private val _bottomSheetState = MutableLiveData(BottomSheetHeightState.Showing) // Initial value
    val bottomSheetState: LiveData<BottomSheetHeightState> = _bottomSheetState

    // Change BottomSheetHeight
    fun showBottomSheet() {
        _bottomSheetState.value = BottomSheetHeightState.Showing
    }
    fun hideBottomSheet() {
        _bottomSheetState.value = BottomSheetHeightState.Hidden
    }


    fun onBadeplassPinClick(
        badeplass: Badeplass) {
        showBottomSheet()
        bottomSheetState
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
        scaffoldState: BottomSheetScaffoldState) {
        if (showBadeplassCard) {
            showBadeplassCard = false
//            showCustomMarker = false
            showBottomSheet()
            coroutineScope.launch { scaffoldState.bottomSheetState.partialExpand() }
        } else {
            showBottomSheet()
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


//    // Variables for SimpleMetAlerts
//    private val _metAlertsForCurrentLocation = MutableLiveData<List<SimpleMetAlert>>(emptyList())
//    val metAlertsForCurrentLocation: LiveData<List<SimpleMetAlert>> = _metAlertsForCurrentLocation
//
//    fun updateMetAlerts(newAlerts: List<SimpleMetAlert>) {
//        _metAlertsForCurrentLocation.value = newAlerts
//    }







    fun loadLocationForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationForecastUiState.update { it.copy(locationForecastUiState = LocationForecastUiState.Loading) }
            locationForecastUiState.update {
                try {
                    val locationForecast = locationForecastRepository.getLocationForecast(lat, lon)
                    it.copy(locationForecastUiState = LocationForecastUiState.Success(locationForecast = locationForecast))
                } catch (e: UnresolvedAddressException) {
                    it.copy(locationForecastUiState = LocationForecastUiState.Error)
                }
            }
        }
    }

    fun loadOceanForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            oceanForecastUiState.update { it.copy(oceanForecastUiState = OceanForecastUiState.Loading) }
            oceanForecastUiState.update {
                try {
                    val oceanForecast = oceanForecastRepository.getOceanForecast(lat, lon)
                    it.copy(oceanForecastUiState = OceanForecastUiState.Success(oceanForecast = oceanForecast))
                } catch (e: UnresolvedAddressException) {
                    it.copy(oceanForecastUiState = OceanForecastUiState.Error)
                }
            }
        }
    }

    fun loadFarevarsler() {
        viewModelScope.launch(Dispatchers.IO) {
            farevarselUiState.update { it.copy(farevarselUiState = FarevarselUiState.Loading) }
            farevarselUiState.update {
                try {
                    val farevarselCollection = farevarselRepository.getFarevarsler()
                    val simpleMetAlertList = farevarselRepository.getSimpleMetAlerts()
                    it.copy(farevarselUiState = FarevarselUiState.Success(
                        farevarselCollection = farevarselCollection,
                        simpleMetAlertList = simpleMetAlertList))
                } catch (e: UnresolvedAddressException) {
                    it.copy(farevarselUiState = FarevarselUiState.Error)
                }
            }
        }
    }


}