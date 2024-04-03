package no.uio.ifi.in2000.martirhe.appsolution.ui.about

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.martirhe.appsolution.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    MaterialTheme(
        colorScheme = lightColorScheme(
            onPrimary = Color.White,
            onSecondary = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("About Plask", fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier
                        .background(Color(0xFF87CEEB)),
                )
            },
            modifier = Modifier.background(Color(0xFF87CEEB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
                    //.background(Color(0xFF87CEEB)) // Sørger for at bakgrunnen er lyseblå

            ) {
                Image(
                    painter = painterResource(id = R.drawable.plasktekst),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .padding(20.dp)
                        .size(width = 210.dp, height = 140.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(1.dp))

                Text("Our Team", color = Color(0xFF0E2D4E), style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.CenterHorizontally)
                ) {
                    Ansatte("Priyanshu", R.drawable.priyanshu)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Vetle", R.drawable.vetle)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Bernd", R.drawable.bernd)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Martine", R.drawable.martine)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Sindre", R.drawable.sindre)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 1.dp)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier
                            .background(Color(0xFF87CEEB)),
                        contentColor = Color.Black
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("About Us") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Contact Us") }
                        )
                    }

                    AnimatedVisibility(
                        visible = selectedTab == 0,
                        enter = fadeIn() + expandIn(),
                        exit = shrinkOut() + fadeOut()
                    ) {
                        AboutUsInfo()
                    }

                    AnimatedVisibility(
                        visible = selectedTab == 1,
                        enter = fadeIn() + expandIn(),
                        exit = shrinkOut() + fadeOut()
                    ) {
                        ContactUsInfo()
                    }
                }
            }
        }
    }
}

@Composable
fun Ansatte(name: String, drawableId: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Ansatte Image",
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        )
        Text(name, color = Color(0xFF0E2D4E))
    }
}

@Composable
fun AboutUsInfo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7DCCE9)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Funksjonalitet", color = Color.Black, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "1. **Badetemperaturer:**\n" +
                    "    - Viser aktuelle badetemperaturer for ulike strender og badeplasser langs kysten.\n" +
                    "    - Mulighet for brukerne å legge til egne badeplasser og dele temperaturmålinger.\n" +
                    "2. **VannVett-reglene:**\n" +
                    "    - Informasjon om VannVett-reglene for trygg ferdsel ved og i vann.\n" +
                    "3. **Værmelding:**\n" +
                    "    - Integrering av Locationforecast API for å gi vær- og vindvarsler for områdene rundt badeplassene.\n" +
                    "    - Varsler om endringer i værforhold som kan påvirke badingen.\n" +
                    "4. **Forventet Badetemperatur:**\n" +
                    "    - Langtidsvarsel for forventet badetemperatur (f.eks. 21 dager fremover) basert på historiske data og værprognoser.\n" +
                    "5. **Legg til Favoritter:**\n" +
                    "    - Mulighet for brukerne å legge til favorittbadeplasser og motta varsler når temperaturen når et ønsket nivå.\n" +
                    "6. **Servering:**\n" +
                    "    - Informasjon om tilgjengelige serveringssteder og fasiliteter i nærheten av badeplassene.\n" +
                    "- Innbydende og brukervennlig grensesnitt med bilder av badeplasser og tydelig presentasjon av vær- og temperaturinformasjon.\n" +
                    "- Interaktivt kart med markører for badeplasser og relevante detaljer.\n" +
                    "- Enkelt system for å legge til og administrere favorittbadeplasser og innstillinger.",
                color = Color(0xFF0E2D4E))
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ContactUsInfo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7DCCE9)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact Us", color = Color.Black, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("info@plask.no", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Icon(Icons.Filled.Phone, contentDescription = "Phone", tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("123 456 7890", color = Color.Black)
            }
        }
    }
}
