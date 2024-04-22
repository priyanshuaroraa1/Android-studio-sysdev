package no.uio.ifi.in2000.martirhe.appsolution.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LocationScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var lastKnownLocation: Location? by remember { mutableStateOf(null) }
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
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
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Permission denied. Unable to use location services.")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
            }) {
                Text("Accept")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                coroutineScope.launch {
                    if (snackbarHostState.showSnackbar(
                            "We strongly recommend activating location services.",
                            actionLabel = "Continue",
                        ) == SnackbarResult.ActionPerformed
                    ) {
                        navController.navigate(Routes.ONBOARDING_SCREEN)
                    }
                }
            }) {
                Text("Decline")
            }
        }
    }
}