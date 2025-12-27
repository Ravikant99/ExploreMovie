package com.ravi.exploremovie.ui.composableItems

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ravi.exploremovie.screenRoutes.ScreenRoutes

@Composable
fun BottomNavigationBar(navController: NavController? = null) {
    val items = listOf("Home", "Search", "Download", "Profile")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Download,
        Icons.Default.Person
    )
    
    // Determine the current route - handle nullable navController properly
    val currentRoute = if (navController != null) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        navBackStackEntry?.destination?.route
    } else {
        null
    }
    
    // Set selected index based on current route
    val selectedIndex = when {
        currentRoute == ScreenRoutes.HomeScreen.route -> 0
        currentRoute == ScreenRoutes.SearchScreen.route -> 1
        currentRoute == ScreenRoutes.DownloadScreen.route -> 2
        currentRoute == ScreenRoutes.ProfileScreen.route -> 3
        currentRoute?.contains("detailsScreen") == true -> -1 // No selection for details screen
        else -> 0 // Default to home
    }

    BottomNavigation(
        backgroundColor = Color(0xFF121021), // Dark background
        contentColor = Color.White,
        elevation = 8.dp
    ) {
        items.forEachIndexed { index, label ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        tint = if (selectedIndex == index) Color(0xFF00F6FF) else Color.Gray
                    )
                },
                label = {
                    if (selectedIndex == index) {
                        Text(text = label, color = Color(0xFF00F6FF))
                    }
                },
                selected = selectedIndex == index,
                onClick = {
                    if (navController != null && selectedIndex != index) {
                        when (index) {
                            0 -> navController.navigate(ScreenRoutes.HomeScreen.route) {
                                popUpTo(ScreenRoutes.HomeScreen.route) { inclusive = false }
                            }
                            1 -> navController.navigate(ScreenRoutes.SearchScreen.route) {
                                popUpTo(ScreenRoutes.HomeScreen.route) { inclusive = false }
                            }
                            2 -> navController.navigate(ScreenRoutes.DownloadScreen.route) {
                                popUpTo(ScreenRoutes.HomeScreen.route) { inclusive = false }
                            }
                            3 -> navController.navigate(ScreenRoutes.ProfileScreen.route) {
                                popUpTo(ScreenRoutes.HomeScreen.route) { inclusive = false }
                            }
                        }
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar()
}
