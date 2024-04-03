package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepository
//import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepositoryInterface
//import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.MetAlertCollection
//import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
//import java.nio.channels.UnresolvedAddressException
//
//
//sealed interface FarevarselUiState {
//    data class Success(
//        val farevarsler: MetAlertCollection,
//        val simpleMetAlerts: List<SimpleMetAlert>): FarevarselUiState
//    object Loading: FarevarselUiState
//    object Error: FarevarselUiState
//}
//
//data class PocFarevarselUiState(
//    val farevarslerState: FarevarselUiState = FarevarselUiState.Loading,
//)
//
//class PocFarevarselViewModel: ViewModel() {
//    val metAlertRepository: MetAlertRepositoryInterface = MetAlertRepository()
//    var uiState = MutableStateFlow(PocFarevarselUiState())
//
//
//
//
//    init {
//        loadFarevarsler()
//
//    }
//
//    fun loadFarevarsler() {
//        viewModelScope.launch(Dispatchers.IO) { // TODO: Riktig med Dispatchers.IO ??
//            uiState.update { it.copy(farevarslerState = FarevarselUiState.Loading) }
//            uiState.update {
//                try {
//                    val farevarsler = metAlertRepository.getMetAlerts()
//                    val simpleMetAlerts: List<SimpleMetAlert> = metAlertRepository.getSimpleMetAlerts()
//                    it.copy(farevarslerState = FarevarselUiState.Success(
//                        farevarsler = farevarsler,
//                        simpleMetAlerts = simpleMetAlerts))
//                } catch (e: UnresolvedAddressException) {
//                    it.copy(farevarslerState = FarevarselUiState.Error)
//                }
//            }
//        }
//    }
//}