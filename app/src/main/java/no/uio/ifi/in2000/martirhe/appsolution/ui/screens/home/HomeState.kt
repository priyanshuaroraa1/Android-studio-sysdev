package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.location.Location
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert

data class HomeState(
    val allSwimspots: List<Swimspot> = emptyList(),
    val lastKnownLocation: Location? = null,
    val selectedSwimspot: Swimspot? = null,
    val customSwimspot: Swimspot? = null,
    val customMarker: Marker? = null,
    val bottomSheetPosition: BottomSheetPosition = BottomSheetPosition.Showing,
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(59.911491,10.757933), 11f), // 5f to show most of Norway, 11f for Oslo

    // For searchbar
    val searchBarText: String = "",
    val searchBarActive: Boolean = false,
    val searchBarHistory: List<String> = emptyList(),

    // For Dialog windows
    val showMetAlertDialog: Boolean = false,
    val metAlertDialogList: List<SimpleMetAlert> = emptyList(),
    val showWeatherInfoDialog: Boolean = false,
    val showAccessibilityInfoDialog: Boolean = false,
)


enum class BottomSheetPosition(val heightDp: Dp) {
    Hidden(0.dp),
    Showing(125.dp), // TODO: Endre til en andel av skjermstørrelsen
} // TODO: Trenger vi i det heletatt denne?