package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.about

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.util.UiEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
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
                    title = { Text("About BadeGuiden",
                        fontSize = 16.sp) },
                    modifier = Modifier
                        .background(Color(0xFF0E2D4E)),
                    navigationIcon = {
                        IconButton(onClick = {
//                            navController.navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(width = 280.dp, height = 140.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier
                    .height(1.dp)
                )

                // Ansatte
                Text("Our Team", color = Color(0xFF0E2D4E), style = MaterialTheme.typography.headlineSmall)
                Row(modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.CenterHorizontally)
                ) {
                    Ansatte("Priyanshu", R.drawable.bilde1)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Vetle", R.drawable.logo)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Bernd", R.drawable.logo)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Martine", R.drawable.bilde2)
                    Spacer(modifier = Modifier.width(8.dp))
                    Ansatte("Sindre", R.drawable.logo)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Valg for About Us og Contact Us
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier
                            .background(color = Color(0xFFF2EDEC))
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

                // Viser About Us og Contact Us
                when (selectedTab) {
                    0 -> AboutUsInfo()
                    1 -> ContactUsInfo()
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
            contentDescription = "Staff Member Image",
            modifier = Modifier.size(64.dp)
        )
        Text(name, color = Color(0xFF0E2D4E))
    }
}

@Composable
fun AboutUsInfo() {
    Text("Lorem Ipsum er rett og slett dummytekst fra og for trykkeindustrien.Lorem Ipsum har vært bransjens standard for dummytekst helt siden 1500-tallet, da en ukjent boktrykker stokket en mengde bokstaver for å lage et prøveeksemplar av en bok.\n\nLorem Ipsum har tålt tidens tann usedvanlig godt, og har i tillegg til å bestå gjennom fem århundrer også tålt spranget over til elektronisk typografi uten vesentlige endringer.\n\nLorem Ipsum ble gjort allment kjent i 1960-årene ved lanseringen av Letraset-ark med avsnitt fra Lorem Ipsum, og senere med sideombrekkingsprogrammet Aldus PageMaker som tok i bruk nettopp Lorem Ipsum for dummytekst.",
        color = Color(0xFF0E2D4E))
}

@Composable
fun ContactUsInfo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EDEC)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact Us", style = MaterialTheme.typography.headlineSmall)
            Text("Email: info@badeguiden.no")
            Text("Phone: (123) 456-7890")
        }
    }
}

