package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot

@Composable
fun SwimspotImage(
    swimspot: Swimspot,
) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))
    AsyncImage(
        model = swimspot.url,
        contentDescription = "Bilde av " + swimspot.spotName,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        contentScale = ContentScale.Crop,
    )
}