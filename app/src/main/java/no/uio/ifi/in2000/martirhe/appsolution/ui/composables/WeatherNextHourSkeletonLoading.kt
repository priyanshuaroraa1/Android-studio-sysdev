package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R

@Composable
fun WeatherNextHourSkeletonLoading() {
    Column(
        Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        SkeletonLoadingCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        SkeletonLoadingCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
        )
    }
}