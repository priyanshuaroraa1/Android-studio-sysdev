package no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.Feature
import java.time.format.TextStyle


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
                    val farevarsler = (farevarslerState).farevarsler
                    FarevarslerLazyColumn(farevarselCollection = farevarsler)
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



@Composable
fun FarevarslerLazyColumn(
    farevarselCollection: FarevarselCollection
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        items(farevarselCollection.features) {feature ->
            FarevarselCard(feature)
        }
    }
}


@Composable
fun FarevarselCard(
    feature: Feature
) {

    Card (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "OBS: Farevarsel!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp))
            Text(text = "Område", fontWeight = FontWeight.ExtraBold)
            Text(text = feature.properties.area,
                modifier = Modifier
                    .padding(bottom = 16.dp))
            Text(text = "Farge", fontWeight = FontWeight.ExtraBold)
            Text(text = feature.properties.awarenessLevel,
                modifier = Modifier
                    .padding(bottom = 16.dp))
            Text(text = "Type", fontWeight = FontWeight.ExtraBold)
            Text(text = feature.properties.awarenessType,
                modifier = Modifier
                    .padding(bottom = 16.dp))
            Text(text = "Melding", fontWeight = FontWeight.ExtraBold)
            Text(text = feature.properties.consequences,
                modifier = Modifier
                    .padding(bottom = 16.dp))
        }
    }
}