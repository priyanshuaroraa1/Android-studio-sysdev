package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import no.uio.ifi.in2000.martirhe.appsolution.data.local.Swimspot

data class SwimspotsState(
    val allSwimspots: List<Swimspot> = emptyList()
)
