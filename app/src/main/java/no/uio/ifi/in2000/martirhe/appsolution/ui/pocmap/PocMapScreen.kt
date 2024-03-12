package no.uio.ifi.in2000.martirhe.appsolution.ui.pocmap

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun PocMapScreen() {
    // Two possible sizes for the card
    val collapsedSize: Dp = 300.dp
    val expandedSize: Dp = 560.dp
    val oslo = LatLng(59.911491, 10.757933)

    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 10f)
    }

    uiSettings = uiSettings.copy(zoomControlsEnabled = true)


    // State to remember if the card is expanded or not
    var isExpanded by remember { mutableStateOf(false) }

    // Height to animate between the two sizes
    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) expandedSize else collapsedSize,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy
        )
    )

    // Full screen Box to layer the map and the card
    Box(modifier = Modifier.fillMaxSize()) {
        // The map goes here - it could be a composable that displays the map
        // Replace "MapComposable()" with your map


        GoogleMap(
            modifier = Modifier,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,

            ) {
            Marker(
                state = MarkerState(position = oslo),
                title = "Oslo",
                snippet = "Marker in Oslo"
            )
        }

        // The card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .align(Alignment.BottomCenter),
//            elevation = 4.dp
        ) {
            // Clickable handle to expand/collapse the card
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Handle",
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .align(Alignment.CenterHorizontally)
            )

            // The rest of your card content goes here, beneath the handle

            // Use 'isExpanded' to control what to show and not to show in different states
        }
    }
}
