package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.location.Location
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert

data class HomeState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val allSwimspots: List<Swimspot> = emptyList(),
    val allMarkers: Map<Int, Marker?> = emptyMap(),
    val map: GoogleMap? = null,
    val userPositionMarker: Marker? = null,
    val lastKnownLocation: Location? = null,
    val selectedSwimspot: Swimspot? = null,
    val customSwimspot: Swimspot? = null,
    val customMarker: Marker? = null,
    val bottomSheetPosition: BottomSheetPosition = BottomSheetPosition.Hidden,
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(59.911491,10.757933), 11f), // 5f to show most of Norway, 11f for Oslo
    val cameraPositionState: CameraPositionState? = null,
    val bottomSheetState: SheetState? = null,

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
    Showing(125.dp),
}