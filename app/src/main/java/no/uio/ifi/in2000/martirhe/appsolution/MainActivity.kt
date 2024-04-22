package no.uio.ifi.in2000.martirhe.appsolution

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.martirhe.appsolution.ui.location.LocationScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar.BottomNavBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.about.AboutScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.onboarding.OnboardingScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSolutionTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.ONBOARDING_SCREEN) {
                    composable(Routes.ONBOARDING_SCREEN) {
                        OnboardingScreen(navController)
                    }
                    composable(Routes.LOCATION_SCREEN) {
                        LocationScreen(navController)
                    }
                }
            }
        }
    }
}

