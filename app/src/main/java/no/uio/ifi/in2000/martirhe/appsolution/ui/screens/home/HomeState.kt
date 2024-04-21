package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home

import android.location.Location
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import java.time.Instant
import java.time.ZonedDateTime

data class HomeState(
    val allSwimspots: List<Swimspot> = emptyList(),
    val lastKnownLocation: Location? = null,
    val selectedSwimspot: Swimspot? = null,
    val customSwimspot: Swimspot? = null,
    val bottomSheetPosition: BottomSheetPosition = BottomSheetPosition.Showing,
    val defaultCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(59.911491,10.757933), 11f), // 5f to show most of country, 11f for Oslo

    // For searchbar
    val searchBarText: String = "",
    val searchBarActive: Boolean = false,
    val searchBarHistory: List<String> = emptyList(),

    // For MetAlertDialog
    val showMetAlertDialog: Boolean = false,
    val metAlertDialogList: List<SimpleMetAlert> = emptyList(),
)


enum class BottomSheetPosition(val heightDp: Dp) {
    Hidden(0.dp),
    Showing(125.dp), // TODO: Endre til en andel av skjermstørrelsen
} // TODO: Trenger vi i det heletatt denne?