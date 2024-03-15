package no.uio.ifi.in2000.martirhe.appsolution.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.martirhe.appsolution.R

@Composable
fun AboutScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7DCCE9)), // Bakgrunnsfarge
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 16.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Om Oss",
                color = Color(0xFF0E2D49),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color(0xFF7DCCE9)), // Bakgrunnsfarge

            ) {
                Text(
                    text = "Her er mer detaljert informasjon om appen eller organisasjonen. Denne teksten kan endres etter behov. Lorem Ipsum er rett og slett dummytekst fra og for trykkeindustrien...",
                    color = Color(0xFF0E2D49), // Tekstfarge
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        // Legg til flere items her om nødvendig
    }
}
