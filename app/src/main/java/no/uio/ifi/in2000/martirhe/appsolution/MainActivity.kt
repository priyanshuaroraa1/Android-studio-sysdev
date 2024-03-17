package no.uio.ifi.in2000.martirhe.appsolution

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.martirhe.appsolution.ui.about.AboutScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.about.TestSideNav
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme

//import com.example.platform.location.permission.LocationPermissionScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSolutionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    PocFarevarselScreen()
//                    PocLocationForecastScreen()
//                    LocationPermissionScreen()
//                    PocMapScreen()

                    AboutNavigasjon()
                }
            }
        }
    }
}

@Composable
fun AboutNavigasjon() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "aboutScreen") {
        composable("aboutScreen") { AboutScreen(navController) }
        composable("testSideNav") { TestSideNav(navController) }
    }
}



