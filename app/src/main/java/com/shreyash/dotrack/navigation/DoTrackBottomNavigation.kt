package com.shreyash.dotrack.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun DoTrackBottomNavigation(
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        bottomNavDestinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any { 
                it.route == destination.route 
            } == true
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = destination.title
                    )
                },
                label = { Text(destination.title) }
            )
        }
    }
}