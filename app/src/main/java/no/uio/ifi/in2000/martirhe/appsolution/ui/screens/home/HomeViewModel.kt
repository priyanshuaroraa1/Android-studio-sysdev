package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertRepositoryInterface
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
    var metAlertUiState: MetAlertUiState by mutableStateOf(MetAlertUiState.Loading)

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.update { swimspotsState -> swimspotsState.copy(allSwimspots = swimspotRepository.getAllSwimspots().first()) }
        }
    }

    fun onSwimspotPinClick(swimspot: Swimspot) {
        updateSelectedSwimspot(swimspot)
        updateBottomSheetPosition(true)
        loadLocationForecast(swimspot.lat, swimspot.lon)
        loadOceanForecast(swimspot.lat, swimspot.lon)
        loadFarevarsler()
        // TODO: Er det noe mer som skal gjøres her?
    }

    fun onMapBackroundClick(
        latLng: LatLng,
        ) {

        if (homeState.value.customSwimspot == null) {
            val customSwimspot = Swimspot(
                id = null,
                googleId = null,
                spotName = "Plassert pin",
                lat = latLng.latitude,
                lon = latLng.longitude,
                accessibility = null,
                locationstring = null,
                original = false,
                favourited = false)

            updateCustomSwimspot(customSwimspot)
            updateSelectedSwimspot(customSwimspot)
            updateBottomSheetPosition(true)
            loadLocationForecast(customSwimspot.lat, customSwimspot.lon)
            loadOceanForecast(customSwimspot.lat, customSwimspot.lon)
            loadFarevarsler()


        } else {
            updateCustomSwimspot(null)
            updateBottomSheetPosition(false)

        }


        // TODO: Implementere
    }

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

    // TODO: Change name/remove yeah
    fun updateBottomSheetPosition(makeVisible: Boolean) {
        val bottomSheetPosition: BottomSheetPosition;
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
        // TODO: Implementere denne
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
                OceanForecastState.Success(oceanForecastRepository.getOceanForecast(lat, lon))
            } catch (e: IOException) {
                OceanForecastState.Error
            } catch (e: ResponseException) {
                OceanForecastState.Error
            } catch (e: UnresolvedAddressException) {
                OceanForecastState.Error
            } catch (e: Error) {
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
            } catch (e: UnresolvedAddressException) {
                MetAlertUiState.Error
            } catch (e: Error) {
                MetAlertUiState.Error
            }
        }
    }
}