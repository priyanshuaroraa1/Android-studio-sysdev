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
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocFarevarsel.PocFarevarselScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.theme.AppSolutionTheme
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.martirhe.appsolution.ui.home.HomeScreen
import no.uio.ifi.in2000.martirhe.appsolution.ui.navbar.BottomNavBar
import no.uio.ifi.in2000.martirhe.appsolution.ui.pocmap.PocMapScreen
import no.uio.ifi.in2000.martirhe.appsolution.util.Routes

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSolutionTheme {

                val navController = rememberNavController()

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
//                            Text(text = "HEI")
                        }
                    }

                }



//                NavHost(
//                    navController = navController,
//                    startDestination = Routes.HOME_SCREEN
//                ) {
//                    composable(Routes.HOME_SCREEN) {
////                        HomeScreen(
////                            onNavigate = {
////                                navController.navigate(it.route)
////                            }
////                        )
//                        Text(text = "Hei")
//                    }
//                }

//
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
////                    PocFarevarselScreen()
////                    PocLocationForecastScreen()
////                    LocationPermissionScreen()
////                    PocMapScreen()
//                    HomeScreen()
//                }
            }
        }
    }
}

//@Composable
//fun BottomNavigationBar(navController: androidx.navigation.NavController) {
//
//    val items = listOf(
//        "Test", "Home", "About"
//    )
//
//    NavigationBar {
//        items.forEach { item ->
//            NavigationBarItem(
//                label = { Text(text = item) },
//                icon = { Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search Icon"
//                ) },
//                selected = true,
//                onClick = { /*TODO*/ })
//        }
//    }
//
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}




