package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel

@Composable
fun AccessibilityOptionsCard(
    accessibilityStringList: List<String>,
    homeViewModel: HomeViewModel,
    outerEdgePaddingValues: Dp,
) {
    Column(
        modifier = Modifier.padding(horizontal = outerEdgePaddingValues)
    ) {
        SmallHeader(text = stringResource(id = R.string.accessability_options_header))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                IconButton(
                    onClick = { homeViewModel.updateShowAccessibilityInfoDialog(true) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = "Mer informasjon")
                }
                Column(
                    modifier = Modifier
                        .padding(all = dimensionResource(id = R.dimen.padding_medium))
                ) {
                    accessibilityStringList.forEachIndexed() { index, string ->
                        if (index != 0) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                        }
                        Row {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Punkt $index",
                                modifier = Modifier
                                    .padding(end = dimensionResource(id = R.dimen.padding_small))
                            )
                            Text(text = string)
                        }
                    }
                }
            }
        }
    }
}