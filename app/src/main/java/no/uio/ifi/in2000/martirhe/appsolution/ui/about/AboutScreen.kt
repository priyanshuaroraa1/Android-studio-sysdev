package no.uio.ifi.in2000.martirhe.appsolution.ui.about

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7DCCE9),
            secondary = Color(0xFF0E2D4E),
            background = Color(0xFFF2EDEC),
            surface = Color(0xFFF2EDEC),
            onPrimary = Color.White,
            onSecondary = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("About BadeGuiden") },
                    modifier = Modifier
                        .background(Color(0xFF7DCCE9))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        //.size(with(LocalDensity.current) { 120.dp.toPx() }, with(LocalDensity.current) { 120.dp.toPx() })
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Staff Members
                Text(
                    "Our Team",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF0E2D4E)
                )
                Row {
                    StaffMember("Priyanshu", R.drawable.logo)
                    StaffMember("Vetle", R.drawable.logo)
                    StaffMember("Bernd", R.drawable.logo)
                    StaffMember("Martine", R.drawable.logo)
                    StaffMember("Sindre", R.drawable.logo)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Tabs for About Us and Contact Us
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFF2EDEC),
                    contentColor = Color(0xFF0E2D4E)
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

                // Display About Us or Contact Us info based on selected tab
                when (selectedTab) {
                    0 -> AboutUsInfo()
                    1 -> ContactUsInfo()
                }
            }
        }
    }
}

@Composable
fun StaffMember(name: String, drawableId: Int) {
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
    Text("About BadeGuiden text goes here...", color = Color(0xFF0E2D4E))
}

@Composable
fun ContactUsInfo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EDEC)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Contact Us",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF0E2D4E)
            )
            Text("Email: info@badeguiden.no")
            Text("Phone: (123) 456-7890")
        }
    }
}

