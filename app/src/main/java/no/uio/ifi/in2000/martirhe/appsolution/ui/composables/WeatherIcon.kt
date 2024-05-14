package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun WeatherIcon(
    iconName: String, smallerSize: Boolean = false
) {
    Box {
        AsyncImage(
            model = "https://raw.githubusercontent.com/metno/weathericons/main/weather/png/$iconName.png",
            contentDescription = iconName,
            modifier = Modifier.size(if (smallerSize) 60.dp else 120.dp)
        )
    }
}