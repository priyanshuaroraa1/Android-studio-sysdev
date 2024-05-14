package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextHour
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel


@Composable
fun WeatherNextHourCard(
    forecastNextHour: ForecastNextHour,
    oceanForecastRightNow: OceanForecastRightNow? = null,
    homeViewModel: HomeViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
    ) {
        SmallHeader(
            text = "Den neste timen",
            paddingBottom = 0.dp,
            paddingTop = 0.dp
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { homeViewModel.updateShowWeatherInfoDialog(true) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Mer informasjon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.weight(1f))

        forecastNextHour.symbolCode?.let { WeatherIcon(it) }
        Spacer(modifier = Modifier.weight(0.5f))
        Column(horizontalAlignment = Alignment.Start) {
            LargeAndSmallText(
                largeText = "${forecastNextHour.getTemperatureString()}° ",
                smallText = "i lufta",
            )

            if (oceanForecastRightNow != null && oceanForecastRightNow.isSaltWater) {
                LargeAndSmallText(
                    largeText = oceanForecastRightNow.getWaterTemperatureString() + "° ",
                    smallText = "i vannet",
                )
            }


        }
        Spacer(modifier = Modifier.weight(1.5f))
    }
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LargeAndSmallText(
                    largeText = forecastNextHour.precipitationAmount.toString(),
                    smallText = "mm",
                )
                Text(
                    text = "Nedbør",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LargeAndSmallText(
                    largeText = forecastNextHour.getWindSpeedString(),
                    smallText = "m/s",
                    image = getFromDirectionPainterResource(forecastNextHour.getWindDirectionString()),
                    imageDescription = "Wind from " + forecastNextHour.getWindDirectionString(),
                )
                Text(
                    text = "Vind",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
            if (oceanForecastRightNow != null && oceanForecastRightNow.isSaltWater) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LargeAndSmallText(
                        largeText = oceanForecastRightNow.getWaveHeightString(),
                        smallText = "cm",
                        image = getFromDirectionPainterResource(oceanForecastRightNow.getWaveDirectionString()),
                        imageDescription = "Waves from " + oceanForecastRightNow.getWaveDirectionString(),
                    )
                    Text(
                        text = "Bølger",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}