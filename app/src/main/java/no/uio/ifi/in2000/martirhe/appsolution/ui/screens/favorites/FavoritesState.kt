package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.favorites

import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot

data class FavoritesState(
    val allFavorites: List<Swimspot> = emptyList(),
)