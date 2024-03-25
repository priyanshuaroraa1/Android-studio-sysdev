package no.uio.ifi.in2000.martirhe.appsolution.model.metalert

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

data class SimpleMetAlert(
    val multiPolygon : List<List<List<List<Float>>>>,
    val area: String,
    val awarenessLevel: String,
    val awarenessType: String,
    val consequences: String,
    val description: String,
)  {

    fun isRelevantForCoordinate(latLng: LatLng): Boolean {

        Log.i("TestCoordinates", "Starting")
//        Log.i("TestCoordinatesx", multiPolygon.toString())
        for (collection in multiPolygon) {
            Log.i("TestCoordinates", "Inni første loop")
//            Log.i("TestCoordinatesx", collection.toString())
            for (polygon in collection) {
                Log.i("TestCoordinates", "Inni andre loop")
                Log.i("TestCoordinatesx", area)
                Log.i("TestCoordinatesx", polygon.toString())

                val polygonPath: MutableList<LatLng> = mutableListOf()
                for (coordList in polygon) {
                    Log.i("TestCoordinates", coordList.toString())
                    polygonPath.add(LatLng(coordList[1].toDouble(), coordList[0].toDouble()))
                }

//                val polygonPath: List<LatLng> = polygon.map {
//                    LatLng(it[0].toDouble(), it[1].toDouble())
//                }
                Log.i("TestCoordinates", polygonPath.toString())

                Log.i("TestCoordinatesx", PolyUtil.containsLocation(latLng, polygonPath, true).toString())
                if (PolyUtil.containsLocation(latLng, polygonPath, true)) {
                    Log.i("TestCoordinates", "true")
                    return true
                }
            }
        }
        Log.i("TestCoordinates", "false")

        return false
    }
}

//   "Green",   "Yellow",       "Orange",   "Red"
//   "Minor",   "Moderate",     "Severe",   "Extreme"

// AwarenessTypes - Kan det finnes flere?
// 1 - Wind (Sterk vind)
// 2 - snow-ice (Mye snø og is)
// 3 - Thunderstorm (Lyn og torden)
// 7 - coastalevent
// 8 - forest-fire
// 10 - rain
// 13 - rain-flood
