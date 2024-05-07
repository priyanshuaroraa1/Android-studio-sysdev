import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.favorites.FavoritesViewModel
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.WarningIconColor
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.LargeHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.MediumHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.MetAlertUiState
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.WarningIcon


@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    favoritesViewModel.updateFavorites()
    val favoritesState = favoritesViewModel.favoritesState.collectAsState().value
    favoritesState.allFavorites

    Column(
        Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {

        LargeHeader(text = "Favoritter")

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            if (favoritesState.allFavorites.isNotEmpty()) {
                items(favoritesState.allFavorites) { swimspot ->
                    FavoriteListItemCard(
                        swimspot = swimspot,
                        onClick = {
                            val route = "${Routes.HOME_SCREEN}?swimspotId=${swimspot.id}"
                            navController.popBackStack(Routes.HOME_SCREEN, false)
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
//                            homeViewModel.updateSelectSwimspotQueue(swimspot) // TODO: Remove this shit
                        },
                        homeViewModel = homeViewModel
                    )
                }
            } else {
                item {
                    Text(text = "Du har ikke lagret noen badeplasser enda.")
                }
            }
        }
    }
}


@Composable
fun FavoriteListItemCard(
    swimspot: Swimspot,
    onClick: () -> Unit,
    homeViewModel: HomeViewModel,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),

        ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {


            MediumHeader(text = swimspot.spotName)
            
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))

            val metAlertUiState = homeViewModel.metAlertUiState.collectAsState().value

            metAlertUiState.let { state ->
                if (state is MetAlertUiState.Success) {
                    val coordinates = swimspot.getLatLng()
                    val simpleMetAlertList = state.simpleMetAlertList.filter {
                        it.isRelevantForCoordinate(coordinates)
                    }
                    val warningIconColor = SimpleMetAlert.mostSevereColor(simpleMetAlertList)
                    if (warningIconColor != WarningIconColor.GREEN) {
                        WarningIcon(
                            warningIconColor = warningIconColor,
                            warningIconDescription = warningIconColor.toString()
                        )
                    }

                }
            }
        }
    }
}
