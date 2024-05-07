package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.notification

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes
import no.uio.ifi.in2000.martirhe.appsolution.util.setRepeatingAlarm

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreen(navController: NavController) {

        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val notificationPermissionGranted = remember { mutableStateOf(false) }
        val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            notificationPermissionGranted.value = true
            coroutineScope.launch {
                CreateNotification(context, "channel_01", "Velkommen til Plask!", "Onboarding fullført. Utforsk appen nå!")
                setRepeatingAlarm(context)  // Sett opp den gjentakende alarmen
                navController.navigate(Routes.HOME_SCREEN)
            }
        } else {
            coroutineScope.launch {
                navController.navigate(Routes.HOME_SCREEN)
            }
        }
    }

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primaryContainer,
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
                        stringResource(id = R.string.notification_screen_label),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.font))
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Image(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Main Illustration",
                        modifier = Modifier
                            .size(280.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        stringResource(id = R.string.notification_screen_subtext),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            if (notificationPermissionGranted.value) {
                                setRepeatingAlarm(context)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.background),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(id = R.string.notification_screen_accept))
                    }
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Du kan aktivere varslingstillatelser senere")
                                navController.navigate(Routes.HOME_SCREEN)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),)
                    {
                        Text(stringResource(id = R.string.location_screen_decline))
                    }
                }
            }
}