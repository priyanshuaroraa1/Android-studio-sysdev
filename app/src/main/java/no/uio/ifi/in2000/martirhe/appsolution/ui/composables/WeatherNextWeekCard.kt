package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.locationforecast.ForecastNextWeek
import no.uio.ifi.in2000.martirhe.appsolution.model.oceanforecast.OceanForecastRightNow

@Composable
fun WeatherNextWeekCard(
    outerEdgePaddingValues: Dp,
    forecastNextWeek: ForecastNextWeek,
    oceanForecastRightNow: OceanForecastRightNow?
) {

    Row {
        Spacer(modifier = Modifier.width(outerEdgePaddingValues))
        SmallHeader(text = "Neste 7 dager")
    }
    LazyRow {
        item {
            Card(
                modifier = Modifier.padding(horizontal = outerEdgePaddingValues),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                ) {

                    forecastNextWeek.weekList.forEach { forecastWeekday ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
                            )
                            Text(
                                text = forecastWeekday.getWeekdayString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            WeatherIcon(forecastWeekday.symbolCode, smallerSize = true)
                            LargeAndSmallText(
                                largeText = forecastWeekday.getTemperatureString() + "° ",
                                smallText = "i lufta",
                                smallerSize = true,
                            )
                            if (oceanForecastRightNow != null) {
                                LargeAndSmallText(
                                    largeText = oceanForecastRightNow.getWaterTemperatureString() + "° ",
                                    smallText = "i vannet",
                                    smallerSize = true,
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
                            )
                        }
                        Spacer(
                            modifier = Modifier.width(40.dp)
                        )
                    }
                }
            }
        }
    }
}