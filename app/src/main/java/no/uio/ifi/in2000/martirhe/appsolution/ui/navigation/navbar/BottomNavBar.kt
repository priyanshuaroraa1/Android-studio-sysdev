package no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes


sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    val route: String,
) {
    object Home :
        BottomNavItem(
            "Hjem",
            Icons.Default.Home,
            Routes.HOME_SCREEN
        )

//    object AboutUs :
//        BottomNavItem(
//            "About us",
//            Icons.Default.Info,
//            Routes.ABOUT_US_SCREEN
//        )

    object Profile :
        BottomNavItem(
            "Profil",
            Icons.Default.Person,
            Routes.PROFILE_SCREEN
        )


    object Favorites :
        BottomNavItem(
            "Favoritter",
            Icons.Default.Star,
            Routes.FAVORITES_SCREEN
        )
}



@Composable
fun BottomNavBar(
    navController: androidx.navigation.NavController
) {

    val items = listOf(
        BottomNavItem.Favorites,
        BottomNavItem.Home,
        BottomNavItem.Profile,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentDestination = navBackStackEntry?.destination

    Box(
        Modifier
            .shadow(elevation = 20.dp)
    ) {

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    label = {
                        Text(text = item.title)
                    },
                    icon = { Icon(
                        imageVector = item.icon,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    ) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    selected = currentRoute == item.route,
                    onClick = {

                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }


//
//                        // Check if the target route is the home screen
//                        if (item.route == Routes.HOME_SCREEN) {
//                            // Pop everything off the navigation stack until we get to the home screen
//                            navController.popBackStack(navController.graph.findStartDestination().id, inclusive = false)
//                        } else {
//                            // When navigating to a screen other than the home screen
//                            // Check if we're not already on that screen
//                            if (currentRoute != item.route) {
//                                navController.navigate(item.route) {
//                                    // Restore state when reselecting a previously selected item
//                                    restoreState = true
//
//                                    // Avoid multiple copies of the same destination
//                                    launchSingleTop = true
//
//                                    // Only pop up to the home destination if it's not the home screen
//                                    // This is to preserve the home screen's state and avoid recreating it
//                                    popUpTo(Routes.HOME_SCREEN) {
//                                        // Keep the home screen as the root and do not pop it
//                                        saveState = true
//                                    }
//                                }
//                            }
//                        }
                    }
                )
            }
        }
    }

}