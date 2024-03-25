package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.MetAlertCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert

sealed interface MetAlertUiState {
    data class Success(
        val MetAlertCollection: MetAlertCollection,
        val simpleMetAlertList: List<SimpleMetAlert>
    ): MetAlertUiState
    object Loading: MetAlertUiState
    object Error: MetAlertUiState
}