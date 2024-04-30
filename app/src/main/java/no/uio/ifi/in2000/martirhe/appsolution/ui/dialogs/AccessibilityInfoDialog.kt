package no.uio.ifi.in2000.martirhe.appsolution.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.composables.MediumHeader
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel

@Composable
fun AccessibilityInfoDialog(
    homeViewModel: HomeViewModel
) {
    Dialog(
        onDismissRequest = { homeViewModel.updateShowAccessibilityInfoDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
                .fillMaxHeight(0.4f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

                )
        ) {

            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.Start
            ) {

                MediumHeader(
                    text = stringResource(id = R.string.accessability_options_header),
                    paddingTop = 0.dp,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small))
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Text(text = stringResource(id = R.string.accessability_options_dialog_body))
                    }
                }

                Button(
                    onClick = { homeViewModel.updateShowAccessibilityInfoDialog(false) },
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