package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeState

@Composable
fun FavoriteIcon(
    homeState: HomeState
) {
    val imageResource = when (homeState.selectedSwimspot?.favourited) {
        true -> painterResource(id = R.drawable.star_yellow)
        else -> painterResource(id = R.drawable.star_white)
    }
    val description = when (homeState.selectedSwimspot?.favourited) {
        true -> "Favoritt"
        else -> "Ikke favoritt"
    }
    Image(painter = imageResource, contentDescription = description)
}
