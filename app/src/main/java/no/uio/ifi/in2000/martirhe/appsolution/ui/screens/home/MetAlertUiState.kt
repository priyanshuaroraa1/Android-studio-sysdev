package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.MetAlertCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert

sealed interface MetAlertUiState {
    data class Success(
        val metAlertCollection: MetAlertCollection,
        val simpleMetAlertList: List<SimpleMetAlert>
    ): MetAlertUiState
    data object Loading: MetAlertUiState
    data object Error: MetAlertUiState
}