package no.uio.ifi.in2000.martirhe.appsolution

import FavoritesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar.BottomNavBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.location.LocationScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.notification.NotificationScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.onboarding.OnboardingScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme
import no.uio.ifi.in2000.martirhe.appsolution.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppSolutionTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val startDestination = if (preferencesManager.isOnboardingShown) Routes.HOME_SCREEN else Routes.ONBOARDING_SCREEN


                Scaffold(
                    bottomBar = {
                        if (currentRoute != Routes.ONBOARDING_SCREEN && currentRoute != Routes.LOCATION_SCREEN && currentRoute != Routes.NOTIFICATION_SCREEN) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.ONBOARDING_SCREEN) {

                            OnboardingScreen(
                                navController,
                                )
                        }
                        composable(Routes.LOCATION_SCREEN) {
                            LocationScreen(navController)
                        }
                        composable(Routes.NOTIFICATION_SCREEN) {
                            NotificationScreen(navController)
                        }
                        composable(Routes.HOME_SCREEN) {
                            HomeScreen(onNavigate = {
                                navController.navigate(it.route)
                            })
                        }
                        composable(Routes.FAVORITES_SCREEN) {
                            FavoritesScreen()
                        }
                    }
                }
            }
        }
    }
}