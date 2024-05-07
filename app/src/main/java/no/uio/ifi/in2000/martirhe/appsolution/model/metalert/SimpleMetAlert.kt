package no.uio.ifi.in2000.martirhe.appsolution.model.metalert

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

data class SimpleMetAlert(
    val multiPolygon : List<List<List<List<Float>>>>,
    val area: String,
    val awarenessLevel: String,
    val awarenessType: String,
    val consequences: String,
    val description: String,
    val eventAwarenessName: String,
)  {

    fun isRelevantForCoordinate(latLng: LatLng): Boolean {
        for (collection in multiPolygon) {
            for (polygon in collection) {
                val polygonPath: MutableList<LatLng> = mutableListOf()
                for (coordList in polygon) {
                    polygonPath.add(LatLng(coordList[1].toDouble(), coordList[0].toDouble()))
                }
                if (PolyUtil.containsLocation(latLng, polygonPath, true)) {
                    return true
                }
            }
        }

        return false
    }

    fun getAwarenesLevelInt(): Int {
        return awarenessLevel.substringBefore(";").toIntOrNull() ?: 0
    }

    fun getAwarenessLevelColor(): WarningIconColor {
        val colorString =  awarenessLevel.split(";").getOrNull(1)?.trim() ?: "green"
        return when (colorString) {
            "yellow" -> WarningIconColor.YELLOW
            "orange" -> WarningIconColor.ORANGE
            "red" -> WarningIconColor.RED
            else -> WarningIconColor.GREEN
        }
    }
    fun getAwarenessLevelDescription(): String {
        return when (getAwarenessLevelColor()) {
            WarningIconColor.RED -> "Rødt farevarsel"
            WarningIconColor.ORANGE -> "Oransje farevarsel"
            WarningIconColor.YELLOW -> "Gult farevarsel"
            WarningIconColor.GREEN -> "Grønt farevarsel"
        }
    }

    companion object {
        fun mostSevereColor(simpleMetAlertList: List<SimpleMetAlert>): WarningIconColor {
            // Find the alert with the maximum severity level
            val mostSevereAlert = simpleMetAlertList.maxByOrNull { simpleMetAlert ->
                simpleMetAlert.getAwarenesLevelInt()
            }

            return if (mostSevereAlert == null) {
                WarningIconColor.GREEN
            } else {
                mostSevereAlert.getAwarenessLevelColor()
            }
        }
    }
}

enum class WarningIconColor(private val colorName: String) {
    GREEN("GRØNN"),
    YELLOW("GUL"),
    ORANGE("ORANSJ"),
    RED("RØD");

    override fun toString(): String {
        return colorName
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
