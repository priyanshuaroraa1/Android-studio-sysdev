package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import no.uio.ifi.in2000.martirhe.appsolution.R

@Composable
fun MediumHeader(
    text: String,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    paddingTop: Dp = dimensionResource(id = R.dimen.padding_large),
    paddingBottom: Dp = dimensionResource(id = R.dimen.padding_small),
) {
    val paddingValues = PaddingValues(
        top = paddingTop,
        bottom = paddingBottom
    )
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(paddingValues)
    )
}