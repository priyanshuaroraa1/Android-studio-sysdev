package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.favorites


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepository

import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val swimspotRepository: SwimspotRepository,
) : ViewModel() {

    private val _favoritesState = MutableStateFlow(FavoritesState())
    val favoritesState = _favoritesState.asStateFlow()

    init {
        updateFavorites()
    }

    fun updateFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoritesState.update { favoritesState ->
                favoritesState.copy(allFavorites = swimspotRepository.getAllFavorites())
            }
        }
    }

}