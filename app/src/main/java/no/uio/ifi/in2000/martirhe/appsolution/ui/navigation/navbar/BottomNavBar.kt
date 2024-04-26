package no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
            "Home",
            Icons.Default.Home,
            Routes.HOME_SCREEN
        )

    object AboutUs :
        BottomNavItem(
            "About us",
            Icons.Default.Info,
            Routes.ABOUT_US_SCREEN
        )

    object Favorites :
        BottomNavItem(
            "Favorites",
            Icons.Default.Checklist,
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
        BottomNavItem.AboutUs,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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

                        if (currentRoute != item.route) {  // Only navigate if not already on the home screen
                            navController.navigate(item.route) {
                                // Clear back stack up to the home destination, but keep the home as the root
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                // Avoid multiple copies of the same destination
                                launchSingleTop = true

                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }

}