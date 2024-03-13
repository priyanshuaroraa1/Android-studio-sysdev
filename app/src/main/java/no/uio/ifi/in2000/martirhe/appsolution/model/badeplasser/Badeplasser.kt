package no.uio.ifi.in2000.martirhe.appsolution.model.badeplasser

import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import kotlin.math.*

data class Badeplasser (
    val badeplasser: List<Badeplass>,

) {
    fun getClosestToCoord(lat: Double, lon: Double, limit: Int = badeplasser.size): List<Badeplass> {
        return badeplasser.sortedBy { haversine(it.lat, it.lon, lat, lon) }.take(limit)
    }


}


fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371 // Earth radius in km

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}