package com.shreyash.dotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shreyash.dotrack.core.ui.theme.DoTrackTheme
import com.shreyash.dotrack.navigation.DoTrackBottomNavigation
import com.shreyash.dotrack.navigation.DoTrackNavHost
import com.shreyash.dotrack.navigation.bottomNavDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoTrackTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                // Check if the current destination is in the bottom nav destinations
                val showBottomBar = currentDestination?.route in bottomNavDestinations.map { it.route }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            DoTrackBottomNavigation(
                                navController = navController,
                                currentDestination = currentDestination
                            )
                        }
                    }
                ) { innerPadding ->
                    DoTrackNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}