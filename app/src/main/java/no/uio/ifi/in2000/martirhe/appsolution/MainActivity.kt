package no.uio.ifi.in2000.martirhe.appsolution

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.martirhe.appsolution.ui.about.About
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme

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
                      val navController = rememberNavController()
                      //AboutScreen(navController)
                      About(navController)
                      //OnboardingScreen(navController)

                }
            }
        }
    }
}




