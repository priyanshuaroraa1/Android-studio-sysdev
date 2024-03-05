package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
fun PocFarevarselScreenPreview() {
    PocFarevarselScreen()
}

@Composable
fun PocFarevarselScreen(

) {

    Column (
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "Overskrift")
        Text(text = "Mer tekst")
        Text(text = "Enda mer tekst")
    }

}