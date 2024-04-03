package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.location.Location
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass

data class HomeScreenUiState(
    val lastKnownLocation: Location?,
    val swimSpots: List<Badeplass> = listOf(),
    var selectedSwimSpot: Badeplass?,
    val customSwimSpot: Badeplass?,
    var bottomSheetPosition: BottomSheetPosition


) {

}


enum class BottomSheetPosition(val heightDp: Dp) {
    Hidden(0.dp),
    Peeking(125.dp), // TODO: Trengs denne?
    Showing(125.dp),
} // TODO: Flytte denne og de andre greiene over til egne filer