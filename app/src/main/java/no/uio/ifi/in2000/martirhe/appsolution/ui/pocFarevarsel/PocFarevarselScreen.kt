package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel



import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PocFarevarselScreen(
    pocFarevarselViewModel: PocFarevarselViewModel = viewModel(),
) {


    Column (
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val uiState by pocFarevarselViewModel.uiState.collectAsState()

        uiState.farevarslerState.let { farevarslerState ->
            when (farevarslerState) {
                is FarevarselUiState.Success -> {
                    Text(text = "Success")
                    val farevarsler = (farevarslerState).farevarsler
//                    Text(text = farevarsler.features.)
                }
                is FarevarselUiState.Loading -> {
                    Text(text = "Loading")
                }
                is FarevarselUiState.Error -> {
                    Text(text = "Error")
                }
            }
        }



    }

}