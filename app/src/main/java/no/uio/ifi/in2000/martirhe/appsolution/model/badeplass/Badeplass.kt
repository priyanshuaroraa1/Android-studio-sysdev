package no.uio.ifi.in2000.martirhe.appsolution.model.badeplass

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class Badeplass(
    val id: String,
    val navn: String,
    var lat: Double,
    var lon: Double,
    val bildeUrl: String? = null,
    val fasiliteter: String? = null,
    val tilgjengelighet: String? = null,
    val beskrivelse: String? = null,
) {
    fun getMarkerOptions(): MarkerOptions {
        return MarkerOptions()
            .position(LatLng(lat, lon))
    }

    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }
}