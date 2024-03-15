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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.LocationForecastUiState
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
                homeViewModel.showBadeplassCard = false
            }
        ) {

            homeViewModel.badeplasser.forEach { badeplass ->
                Marker(
                    state = MarkerState(position = LatLng(badeplass.lat, badeplass.lon)),
                    onClick = {
                        homeViewModel.onBadeplassPinClick(badeplass)
                        false
                    }
                )
            }

        }

        if (homeViewModel.showBadeplassCard) {

            BadeplassInfoCard(
                homeViewModel = homeViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter),
            )



            
        }


    }
}

@Composable
fun BadeplassInfoCard(
    homeViewModel: HomeViewModel,
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

        val uiState by homeViewModel.locationForecastUiState.collectAsState()

        uiState.locationForecastUiState.let { state ->
            when (state) {
                is LocationForecastUiState.Success -> {


                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {

                        Text(
                            text = homeViewModel.selectedBadeplass.navn,
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            fontSize = 18.sp
                        )

                        Text(text = "Temperatur: " + state.locationForecast.properties.timeseries[0].data.instant.details.air_temperature)




                    }

                }

                is LocationForecastUiState.Loading -> {
                    Text(text = "Loading")
                }

                is LocationForecastUiState.Error -> {
                    Text(text = "Error")
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
