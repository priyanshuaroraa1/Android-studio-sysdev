package no.uio.ifi.in2000.martirhe.appsolution.ui.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector
) {
    object Home :
        BottomNavItem(
            "Home",
            Icons.Default.Home
        )

    object AboutUs :
        BottomNavItem(
            "About us",
            Icons.Default.Info
        )

    object List :
        BottomNavItem(
            "List",
            Icons.Default.Checklist
        )
}



@Composable
fun BottomNavBar(navController: androidx.navigation.NavController) {

    val items = listOf(
        BottomNavItem.List,
        BottomNavItem.Home,
        BottomNavItem.AboutUs,
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                label = {
                        Text(text = item.title)
                },
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = "Search Icon"
                ) },
                selected = true,
                onClick = { /*TODO*/ })
        }
    }

}