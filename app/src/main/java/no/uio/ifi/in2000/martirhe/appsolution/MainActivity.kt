package no.uio.ifi.in2000.martirhe.appsolution

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.martirhe.appsolution.ui.about.AboutNy
import no.uio.ifi.in2000.martirhe.appsolution.ui.onboarding.OnboardingScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.about.AboutUsScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar.BottomNavBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSolutionTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME_SCREEN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.HOME_SCREEN) {
                            HomeScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                        composable(Routes.ABOUT_US_SCREEN) {
                            AboutUsScreen(onNavigate = {
                                navController.navigate(it.route)
                            })
                        }
                    }
                }
            }
        }
    }
}
