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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.viewmodel.compose.viewModel
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

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        var locationPermissionGranted by remember { mutableStateOf(false) }
        var lastKnownLocation: Location? by remember { mutableStateOf(null) }
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val viewModel: LocationViewModel = viewModel()

        LaunchedEffect(Unit) {
            viewModel.fetchLocation()
        }

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                locationPermissionGranted = true
                coroutineScope.launch {
                    try {
                        val location = fusedLocationClient.lastLocation.await()
                        if (location != null) {
                            lastKnownLocation = location
                            navController.navigate(Routes.NOTIFICATION_SCREEN)
                        } else {
                            lastKnownLocation = null
                            navController.navigate(Routes.NOTIFICATION_SCREEN)
                        }
                    } catch (e: Exception) {
                        snackbarHostState.showSnackbar("Feil med å finne posisjonen din: ${e.localizedMessage}", duration = SnackbarDuration.Short)
                        navController.navigate(Routes.NOTIFICATION_SCREEN)
                    }
                }
            } else {
                coroutineScope.launch {
                    navController.navigate(Routes.NOTIFICATION_SCREEN)
                }
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = "Plask logo",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(12.dp)
                    )
                }
            },
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
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = FontFamily(Font(R.font.font))
                    ),
                    textAlign = TextAlign.Center,
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier
                    .height(32.dp)
                    .size(32.dp))

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
                                snackbarHostState.showSnackbar("Kunne ikke finne posisjon")
                            }
                        }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer, contentColor = MaterialTheme.colorScheme.background),
                    shape = MaterialTheme.shapes.medium) {
                    Text(stringResource(id = R.string.location_screen_accept))
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        if (snackbarHostState.showSnackbar(
                                "Vi anbefaler å aktivere lokasjonstjenester.",
                                actionLabel = "Gå videre uten",
                            ) == SnackbarResult.ActionPerformed
                        ) {
                            navController.navigate(Routes.NOTIFICATION_SCREEN)
                        }
                    }
                },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),)
                {
                    Text(stringResource(id = R.string.location_screen_decline))
                }
            }
        }
    }
