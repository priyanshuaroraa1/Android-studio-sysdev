package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R


@Preview(showBackground = true)
@Composable
fun PreviewLargeAndSmallText() {
    LargeAndSmallText(
        largeText = "19",
        smallText = "mm",
        image = painterResource(id = R.drawable.north),
        imageDescription = "Wind from north",
    )
}


@Composable
fun LargeAndSmallText(

    largeText: String,
    smallText: String,
    image: Painter? = null,
    imageDescription: String = "",
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    smallerSize: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (image != null) {

            Image(
                painter = image,
                contentDescription = imageDescription,
                modifier = Modifier.size(16.dp) // Set size or other modifiers as needed
            )
        }

        val largeStyle =
            if (smallerSize) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineSmall
        val smallStyle =
            if (smallerSize) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyLarge
        Text(
            text = largeText,
            color = color,
            style = largeStyle,
        )
        Text(
            text = smallText,
            color = color,
            style = smallStyle,
            modifier = Modifier.padding(bottom = if (smallerSize) 0.dp else 3.dp)
        )
    }
}