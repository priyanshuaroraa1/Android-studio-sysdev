package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel.FarevarselRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.SimpleMetAlert
import java.nio.channels.UnresolvedAddressException


sealed interface FarevarselUiState {
    data class Success(
        val farevarsler: FarevarselCollection,
        val simpleMetAlerts: List<SimpleMetAlert>): FarevarselUiState
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
                    val simpleMetAlerts: List<SimpleMetAlert> = farevarselRepository.getSimpleMetAlerts()
                    it.copy(farevarslerState = FarevarselUiState.Success(
                        farevarsler = farevarsler,
                        simpleMetAlerts = simpleMetAlerts))
                } catch (e: UnresolvedAddressException) {
                    it.copy(farevarslerState = FarevarselUiState.Error)
                }
            }
        }
    }
}