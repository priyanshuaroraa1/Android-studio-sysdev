package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.notification

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val notificationPermissionGranted = remember { mutableStateOf(false) }

    val TAG = "NotificationScreen"

    // Define the permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d(TAG, "Permission request result: $isGranted")
        if (isGranted) {
            notificationPermissionGranted.value = true
            coroutineScope.launch {
                Log.d(TAG, "Notification permission granted")
                snackbarHostState.showSnackbar("Notification permission granted.")
                navController.navigate(Routes.HOME_SCREEN) // Assuming "homeScreen" is your destination after permissions
            }
        } else {
            coroutineScope.launch {
                Log.d(TAG, "Notification permission denied")
                snackbarHostState.showSnackbar("Notification permission denied.")
            }
        }
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7DCCE9),
            secondary = Color(0xFF0E2D4E),
            tertiary = Color(0xFFF2EDEC),
            onPrimary = Color.White
        )
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
                    "Notification Permission",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(id = R.drawable.pin), // Use an appropriate image
                    contentDescription = "Notification Illustration",
                    modifier = Modifier.size(250.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "We need permission to send you notifications for alerts and updates.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    Log.d(TAG, "Requesting POST_NOTIFICATIONS permission")
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = MaterialTheme.shapes.medium) {
                    Text("Accept")
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        Log.d(TAG, "Decline button clicked")
                        snackbarHostState.showSnackbar("You can enable notification permissions in settings.")
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = MaterialTheme.shapes.medium) {
                    Text("Decline", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}
