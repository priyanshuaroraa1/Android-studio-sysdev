package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection
import java.nio.channels.UnresolvedAddressException


sealed interface FarevarselUiState {
    data class Success(val farevarsler: FarevarselCollection): FarevarselUiState
    object Loading: FarevarselUiState
    object Error: FarevarselUiState
}

data class PocFarevarselUiState(
    val farevarslerState: FarevarselUiState = FarevarselUiState.Loading,
)

class PocFarevarselViewModel: ViewModel() {
    val farevarselRepository: FarevarselRepositoryInterface = FarevarselRepository()
    var uiState = MutableStateFlow(PocFarevarselUiState())




    init {
        loadFarevarsler()
    }

    fun loadFarevarsler() {
        viewModelScope.launch(Dispatchers.IO) { // TODO: Riktig med Dispatchers.IO ??
            uiState.update { it.copy(farevarslerState = FarevarselUiState.Loading) }
            uiState.update {
                try {
                    val farevarsler = farevarselRepository.getFarevarsler()
                    it.copy(farevarslerState = FarevarselUiState.Success(farevarsler = farevarsler))
                } catch (e: UnresolvedAddressException) {
                    it.copy(farevarslerState = FarevarselUiState.Error)
                }
            }
        }
    }
}