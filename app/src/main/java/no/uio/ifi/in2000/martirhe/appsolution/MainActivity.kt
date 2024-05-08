package no.uio.ifi.in2000.martirhe.appsolution

import FavoritesScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar.BottomNavBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.about.AboutScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.location.LocationScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.onboarding.OnboardingScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.profile.ProfileScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.watersafetyrules.WaterSafetyRulesScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme
import no.uio.ifi.in2000.martirhe.appsolution.util.NetworkLiveData
import no.uio.ifi.in2000.martirhe.appsolution.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkLiveData = NetworkLiveData(applicationContext)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            val isNetworkAvailable = networkLiveData.observeAsState(initial = true).value  // Observe nettverkstilstand

            AppSolutionTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val startDestination =
                    if (preferencesManager.isOnboardingShown) Routes.HOME_SCREEN else Routes.ONBOARDING_SCREEN


                Scaffold(
                    bottomBar = {
                        if (currentRoute != Routes.ONBOARDING_SCREEN && currentRoute != Routes.LOCATION_SCREEN) {
                            BottomNavBar(navController = navController)
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.ONBOARDING_SCREEN) {
                            OnboardingScreen(navController)

                        }
                        composable(Routes.LOCATION_SCREEN) {
                            LocationScreen(navController)
                        }
                        composable(Routes.HOME_SCREEN) {
                            HomeScreen()
                        }
                        composable(
                            Routes.HOME_SCREEN_WITH_ID,
                            arguments = listOf(navArgument("swimspotId") {
                                type = NavType.IntType
                                defaultValue = -1  // Assuming -1 means no ID was passed
                            })
                        ) {
                            // Extract the optional swimspotId argument and use it in HomeScreen
                            HomeScreen(swimspotId = it.arguments?.getInt("swimspotId") ?: -1)
                        }
                        composable(Routes.FAVORITES_SCREEN) {
                            FavoritesScreen(navController = navController)
                        }
                        composable(Routes.ABOUT_US_SCREEN) {
                            AboutScreen(navController = navController)
                        }
                        composable(Routes.WATERSAFETYRULES_SCREEN) {
                            WaterSafetyRulesScreen(navController = navController)
                        }
                        composable(Routes.PROFILE_SCREEN) {
                            ProfileScreen(
                                navController = navController
                            )
                        }
                    }
                    // Observasjon og håndtering av nettverkstilstand
                    LaunchedEffect(isNetworkAvailable) {
                        if (!isNetworkAvailable) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Ingen internettforbindelse. Noen funksjoner vil ikke være tilgjengelige",
                                    actionLabel = "Fortsett",
                                    duration = SnackbarDuration.Long
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    // Lukker snackbar
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
