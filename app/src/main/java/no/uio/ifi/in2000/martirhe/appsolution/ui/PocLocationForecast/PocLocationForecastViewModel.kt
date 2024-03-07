package no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.LocationForecast
import java.nio.channels.UnresolvedAddressException


sealed interface LocationForecastUiState {
    data class Success(
        val locationForecast: LocationForecast
    ): LocationForecastUiState
    object Loading: LocationForecastUiState
    object Error: LocationForecastUiState
}

data class PocLocationForecastUiState(
    val locationForecastUiState: LocationForecastUiState = LocationForecastUiState.Loading
)

 class PocLocationForecastViewModel: ViewModel() {
     val locationForecastRepository: LocationForecastRepositoryInterface = LocationForecastRepository()
     var uiState = MutableStateFlow(PocLocationForecastUiState())

     var latitude by mutableDoubleStateOf(50.911491)
     var longitude by mutableDoubleStateOf(12.757933)
     var showForecast by mutableStateOf(false)
     var chosenCity by mutableStateOf("Oslo")


     init {
         // TODO: Do we need to do anything in the init?
     }

     fun loadLocationForecast(lat: Double, lon: Double) {
         viewModelScope.launch(Dispatchers.IO) {
             uiState.update { it.copy(locationForecastUiState = LocationForecastUiState.Loading) }
             uiState.update {
                 try {
                     val locationForecast = locationForecastRepository.getLocationForecast(lat, lon)
                     it.copy(locationForecastUiState = LocationForecastUiState.Success(locationForecast = locationForecast))
                 } catch (e: UnresolvedAddressException) {
                     it.copy(locationForecastUiState = LocationForecastUiState.Error)
                 }
             }
         }
     }

}