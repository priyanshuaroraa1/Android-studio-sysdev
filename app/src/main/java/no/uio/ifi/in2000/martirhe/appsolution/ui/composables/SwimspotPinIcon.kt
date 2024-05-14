package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R

@Composable
fun SwimspotPinIcon(
    painter: Painter = painterResource(id = R.drawable.pin),
    description: String = "Plask logo"
) {
    Image(
        painter = painter,
        contentDescription = description,
        modifier = Modifier
            .height(32.dp),
    )
}
