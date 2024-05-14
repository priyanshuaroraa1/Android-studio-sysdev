package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert

@Composable
fun MetAlertCard(
    simpleMetAlertList: List<SimpleMetAlert>,
    onClick: () -> Unit,
) {
    val numberOfAlerts = simpleMetAlertList.size
    val warningIconColor = SimpleMetAlert.mostSevereColor(simpleMetAlertList)
    val warningIconDescription = SimpleMetAlert.mostSevereColor(simpleMetAlertList)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
        ) {
            WarningIcon(
                warningIconColor, warningIconDescription.toString()
            )
            Text(
                text = "$numberOfAlerts aktive farevarsler",
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Se farevarsler",
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}