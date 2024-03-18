package no.uio.ifi.in2000.martirhe.appsolution.ui.aboutTEST

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R

// Direkte definisjon av fargekoder
private val PrimaryColor = Color(0xFF7DCCE9)
private val SecondaryColor = Color(0xFF0E2D4E)
private val TertiaryColor = Color(0xFFF2EDEC)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryColor,
            secondary = SecondaryColor,
            background = TertiaryColor,
            surface = TertiaryColor,
            onPrimary = Color.White,
            onSecondary = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("About BadeGuiden") },
                    modifier = Modifier
                        .background(color = PrimaryColor)
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

                // Staff Members
                Text("Our Team", color = SecondaryColor, style = MaterialTheme.typography.headlineSmall)
                Row(modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.CenterHorizontally)
                ) {
                    StaffMember("Priyanshu", R.drawable.bilde1)
                    Spacer(modifier = Modifier.width(8.dp))
                    StaffMember("Vetle", R.drawable.logo)
                    Spacer(modifier = Modifier.width(8.dp))
                    StaffMember("Bernd", R.drawable.logo)
                    Spacer(modifier = Modifier.width(8.dp))
                    StaffMember("Martine", R.drawable.logo)
                    Spacer(modifier = Modifier.width(8.dp))
                    StaffMember("Sindre", R.drawable.logo)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Tabs for About Us and Contact Us
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier
                            .background(color = TertiaryColor)
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
        Text(name, color = SecondaryColor)
    }
}

@Composable
fun AboutUsInfo() {
    Text("About BadeGuiden text goes here...", color = SecondaryColor)
}

@Composable
fun ContactUsInfo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = TertiaryColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact Us", style = MaterialTheme.typography.headlineSmall)
            Text("Email: info@badeguiden.no")
            Text("Phone: (123) 456-7890")
        }
    }
}

