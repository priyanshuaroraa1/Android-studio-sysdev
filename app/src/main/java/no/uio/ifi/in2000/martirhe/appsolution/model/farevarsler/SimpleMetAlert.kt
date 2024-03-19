package no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.DataPolygon

data class SimpleMetAlert(
    val multiPolygon : List<List<List<List<Float>>>>,
    val area: String,
    val awarenessLevel: String,
    val awarenessType: String,
    val consequences: String,
    val description: String,
)

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
