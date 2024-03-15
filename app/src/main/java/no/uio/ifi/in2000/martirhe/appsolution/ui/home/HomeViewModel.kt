package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecast
import no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.PocLocationForecastUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselUiState
import java.nio.channels.UnresolvedAddressException


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

data class PocLocationForecastUiState(
    val locationForecastUiState: LocationForecastUiState = LocationForecastUiState.Loading
)
data class PocOceanForecastUiState(
    val oceanForecastUiState: OceanForecastUiState = OceanForecastUiState.Loading
)

class HomeViewModel : ViewModel() {

    val locationForecastRepository: LocationForecastRepositoryInterface = LocationForecastRepository()
    var locationForecastUiState = MutableStateFlow(PocLocationForecastUiState())

    val oceanForecastRepository: OceanForecastRepositoryInterface = OceanForecastRepository()
    var oceanForecastUiState = MutableStateFlow(PocOceanForecastUiState())
    // Dummy data
    val badeplasserDummy: List<Badeplass> = listOf(
        Badeplass("00", "Huk", 59.895002996529485, 10.67554858599053),
        Badeplass("01", "Katten", 59.85526122555474, 10.783288859002855),
        Badeplass("02", "Hovedøya", 59.89531481816671, 10.724690847364073),
        Badeplass("03", "Tjuvholmen", 59.9064275008578, 10.721101654359384),
    )
    val badeplasser = badeplasserDummy


    // Variables for Map
    var selectedBadeplass by mutableStateOf<Badeplass>(badeplasser[0])
    var showBadeplassCard by mutableStateOf(false)

    fun onBadeplassPinClick(badeplass: Badeplass) {
        selectedBadeplass = badeplass
        showBadeplassCard = true
        loadLocationForecast(badeplass.lat, badeplass.lon)
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
        searchBarText = ""
        searchBarActive = false
    }

    fun loadLocationForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationForecastUiState.update { it.copy(locationForecastUiState = no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.LocationForecastUiState.Loading) }
            locationForecastUiState.update {
                try {
                    val locationForecast = locationForecastRepository.getLocationForecast(lat, lon)
                    it.copy(locationForecastUiState = no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.LocationForecastUiState.Success(locationForecast = locationForecast))
                } catch (e: UnresolvedAddressException) {
                    it.copy(locationForecastUiState = no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.LocationForecastUiState.Error)
                }
            }
        }
    }


}