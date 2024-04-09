package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.location.Location
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.martirhe.appsolution.data.local.Swimspot

data class HomeState(
    val allSwimspots: List<Swimspot> = emptyList(),
    val lastKnownLocation: Location? = null,
    val selectedSwimspot: Swimspot? = null,
    val customSwimspot: Swimspot? = null,
    val bottomSheetPosition: BottomSheetPosition = BottomSheetPosition.Showing,
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(59.911491,10.757933), 11f),

    // For searchbar
    val searchBarText: String = "",
    val searchBarActive: Boolean = false,
    val searchBarHistory: List<String> = emptyList(),
)


enum class BottomSheetPosition(val heightDp: Dp) {
    Hidden(0.dp),
    Showing(125.dp), // TODO: Endre til en andel av skjermstørrelsen
} // TODO: Trenger vi i det heletatt denne?