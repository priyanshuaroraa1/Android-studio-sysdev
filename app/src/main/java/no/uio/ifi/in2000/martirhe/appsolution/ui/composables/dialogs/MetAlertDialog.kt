package no.uio.ifi.in2000.martirhe.appsolution.ui.composables.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.MediumHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.SmallHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.WarningIcon
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel

@Composable
fun MetAlertDialog(
    homeViewModel: HomeViewModel,
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    Dialog(
        onDismissRequest = { homeViewModel.updateShowMetAlertDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .fillMaxHeight(0.5f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

                )
        ) {

            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val headerString = if (homeState.selectedSwimspot?.spotName != "Plassert pin") {
                    "Farevarsler for ${homeState.selectedSwimspot?.spotName}"
                } else {
                    "Farevarsler for plassert pin"
                }
                MediumHeader(
                    text = headerString, paddingTop = 0.dp
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    homeState.metAlertDialogList.sortedByDescending { it.getAwarenesLevelInt() }
                        .takeIf { it.isNotEmpty() }?.let { sortedList ->
                            itemsIndexed(sortedList) { index, simpleMetAlert ->

                                if (index > 0) {
                                    Spacer(
                                        modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium))
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        WarningIcon(
                                            warningIconColor = simpleMetAlert.getAwarenessLevelColor(),
                                            warningIconDescription = simpleMetAlert.getAwarenessLevelDescription()
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                                        SmallHeader(
                                            text = simpleMetAlert.getAwarenessLevelColor().toString() + ": " + simpleMetAlert.eventAwarenessName + ": " + simpleMetAlert.area,
                                            paddingTop = 0.dp,
                                            paddingBottom = 0.dp
                                        )

                                    }
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                                    Text(text = simpleMetAlert.description)
                                }
                            }
                        } ?: item {
                        Text(text = "Det er ingen aktive farevarsler nå.")
                    }
                }

                Button(
                    onClick = { homeViewModel.updateShowMetAlertDialog(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Lukk")
                }
            }
        }
    }
}