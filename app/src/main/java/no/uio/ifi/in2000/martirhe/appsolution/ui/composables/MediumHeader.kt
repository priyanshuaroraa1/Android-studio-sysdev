package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MediumHeader(
    text: String,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
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