package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LocationScreen(navController: NavController) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7DCCE9),
            secondary = Color(0xFF0E2D4E),
            tertiary = Color(0xFFF2EDEC),
            onPrimary = Color.White,
        )
    ) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        var locationPermissionGranted by remember { mutableStateOf(false) }
        var lastKnownLocation: Location? by remember { mutableStateOf(null) }
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                locationPermissionGranted = true
                coroutineScope.launch {
                    try {
                        val location = fusedLocationClient.lastLocation.await()
                        if (location != null) {
                            // Oppdaterer lastKnownLocation etter å sjekket null-verdi
                            lastKnownLocation = location
                            // Viser posisjon
                            snackbarHostState.showSnackbar("Posisjonen din er godkjent!")
                            snackbarHostState.showSnackbar("Godkjent - Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                            navController.navigate(Routes.NOTIFICATION_SCREEN)
                        } else {
                            // Vis man ikke finner posisjon
                            lastKnownLocation = null
                            snackbarHostState.showSnackbar("Posisjon ikke funnet.")
                        }
                    } catch (e: Exception) {
                        // Exception handling
                        snackbarHostState.showSnackbar("Feil med å finne posisjon: ${e.localizedMessage}")
                    }
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Tillatelse avslått.")
                }
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    stringResource(id = R.string.location_screen_label),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(
                        Font(R.font.font1)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Main Illustration",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    stringResource(id = R.string.location_screen_subtext),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier
                    .height(32.dp)
                    .size(32.dp))

                //Knapper
                Button(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationPermissionGranted = true
                        coroutineScope.launch {
                            try {
                                val location = fusedLocationClient.lastLocation.await()
                                lastKnownLocation = location
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error obtaining location.")
                            }
                        }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = MaterialTheme.shapes.medium) {
                    Text(stringResource(id = R.string.location_screen_accept))
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        if (snackbarHostState.showSnackbar(
                                "We strongly recommend activating location services.",
                                actionLabel = "Go ahead anyways",
                            ) == SnackbarResult.ActionPerformed
                        ) {
                            navController.navigate(Routes.NOTIFICATION_SCREEN)
                        }
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = MaterialTheme.shapes.medium) {
                    Text(stringResource(id = R.string.location_screen_decline))
                }
            }
        }
    }
}