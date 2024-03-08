package no.uio.ifi.in2000.martirhe.appsolution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import no.uio.ifi.in2000.martirhe.appsolution.ui.PocLocationForecast.PocLocationForecastScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
//import com.example.platform.location.permission.LocationPermissionScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)                 // TODO: Hva gjør denne?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSolutionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Kommenter ut denne....
//                    PocFarevarselScreen()
//                    ... og kommenter inn denne i stedet for å sjekke værmelding
                    PocLocationForecastScreen()
//                    LocationPermissionScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppSolutionTheme {
        Greeting("Android")
    }
}


