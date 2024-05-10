package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.favorites

import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot

data class FavoritesState(
    val allFavorites: List<Swimspot> = emptyList(),
)