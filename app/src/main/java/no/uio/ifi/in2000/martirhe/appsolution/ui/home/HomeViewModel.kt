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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselUiState


class HomeViewModel : ViewModel() {

    // Dummy data
    val badeplasserDummy: List<Badeplass> = listOf(
        Badeplass("00", "Huk", 59.895002996529485, 10.67554858599053),
        Badeplass("01", "Katten", 59.85526122555474, 10.783288859002855),
        Badeplass("02", "Hovedøya", 59.89531481816671, 10.724690847364073),
        Badeplass("03", "Tjuvholmen", 59.9064275008578, 10.721101654359384),
    )
    val badeplasser = badeplasserDummy


    // Variables for Map
    var selectedBadeplass by mutableStateOf<Badeplass?>(null)



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


}