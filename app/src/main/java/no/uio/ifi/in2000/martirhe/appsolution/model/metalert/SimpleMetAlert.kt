package no.uio.ifi.in2000.martirhe.appsolution.model.metalert

import android.util.Log
import androidx.compose.ui.graphics.Color
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
        for (collection in multiPolygon) {
            Log.i("TestCoordinates", "Inni første loop")
            for (polygon in collection) {
                Log.i("TestCoordinates", "Inni andre loop")
                Log.i("TestCoordinatesx", area)
                Log.i("TestCoordinatesx", polygon.toString())

                val polygonPath: MutableList<LatLng> = mutableListOf()
                for (coordList in polygon) {
                    Log.i("TestCoordinates", coordList.toString())
                    polygonPath.add(LatLng(coordList[1].toDouble(), coordList[0].toDouble()))
                }

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

    fun getAwarenesLevelInt(): Int {
        return awarenessLevel.substringBefore(";").toIntOrNull() ?: 0
    }

    fun getAwarenessLevelColor(): Color {
        val colorString =  awarenessLevel.split(";").getOrNull(1)?.trim() ?: "green"
        return when (colorString) {
            "yellow" -> Color.Yellow
            "orange" -> Color(0xFFFFA500)
            "red" -> Color.Red
            else -> Color.Green
        }
    }
    companion object {
        fun mostSevereColor(simpleMetAlertList: List<SimpleMetAlert>): Color {
            // Find the alert with the maximum severity level
            val mostSevereAlert = simpleMetAlertList.maxByOrNull { simpleMetAlert ->
                simpleMetAlert.getAwarenesLevelInt()
            }

            return if (mostSevereAlert == null) {
                Color.Green
            } else {
                mostSevereAlert.getAwarenessLevelColor()
            }
        }
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
