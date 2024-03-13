package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop169
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.ktx.model.cameraPosition
import no.uio.ifi.in2000.martirhe.appsolution.model.badeplass.Badeplass
import java.time.temporal.TemporalQuery


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {


    val oslo = LatLng(59.911491, 10.757933)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 10f)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // The map goes here - it could be a composable that displays the map
        // Replace "MapComposable()" with your map


        GoogleMap(
            modifier = Modifier,
            cameraPositionState = cameraPositionState,
            onMapClick = {
                homeViewModel.selectedBadeplass = null
            }
        ) {

            homeViewModel.badeplasser.forEach { badeplass ->
                Marker(
                    state = MarkerState(position = LatLng(badeplass.lat, badeplass.lon)),
                    onClick = {
                        homeViewModel.selectedBadeplass = badeplass
                        false
                    }
                )
            }

        }

        if (homeViewModel.selectedBadeplass != null) {
            BadeplassInfoCard(
                badeplass = homeViewModel.selectedBadeplass!!,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }


    }
}

@Composable
fun BadeplassInfoCard(
    badeplass: Badeplass,
    modifier: Modifier,
) {
    Card(
        modifier = modifier
            .padding(32.dp)
            .padding(bottom = 32.dp)
            .fillMaxWidth()
            .height(220.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Text(text = badeplass.navn,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                fontSize = 18.sp
            )

            Row(
                modifier = Modifier
                .weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()

                ) {
                    Text(
                        text = "Temp: ",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Skydekke: ",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Vind: ",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Vindretning: ",
                        fontSize = 14.sp
                    )

                }
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()

                ) {
                    Text(text = "tmp")
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Text(text = "tmp")
                }
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxSize()

                ) {
                    Text(text = "tmp")
                }
            }






        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MapSearchBar(
    homeViewModel: HomeViewModel = viewModel()
) {
    SearchBar(
        query = homeViewModel.searchBarText,
        onQueryChange = { homeViewModel.searchBarText = it },
        onSearch = { homeViewModel.onSearch() },
        active = homeViewModel.searchBarActive,
        onActiveChange = { homeViewModel.searchBarActive = it },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(text = "Finn badeplass")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            ) // TODO: Description?
        },
        trailingIcon = {
            if (homeViewModel.searchBarActive) {
                Icon(
                    modifier = Modifier.clickable {
                        if (homeViewModel.searchBarText.isNotEmpty()) {
                            homeViewModel.searchBarText = ""
                        } else {
                            homeViewModel.searchBarActive = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }

        },

        ) {
        homeViewModel.searchBarHistory.forEach {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        homeViewModel.searchBarText = it
                        homeViewModel.onSearch()
                    }
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "History Icon"
                )
                Text(text = it)

            }
        }
    }
}


// Her er den store TODO-lista
// TODO: Lagre tidligere søk i en lokal eller ekstern database
