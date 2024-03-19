package no.uio.ifi.in2000.martirhe.appsolution.model.badeplass

data class Badeplass(
    val id: String,
    val navn: String,
    var lat: Double,
    var lon: Double,
    val bildeUrl: String? = null,
    val fasiliteter: String? = null,
    val tilgjengelighet: String? = null,
    val beskrivelse: String? = null,
)