package com.shreyash.dotrack

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shreyash.dotrack.core.ui.theme.DoTrackTheme
import com.shreyash.dotrack.core.util.switchAppIcon
import com.shreyash.dotrack.navigation.DeepLinkHandler
import com.shreyash.dotrack.navigation.DoTrackBottomNavigation
import com.shreyash.dotrack.navigation.DoTrackNavHost
import com.shreyash.dotrack.navigation.bottomNavDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Store the intent to handle it after the UI is ready
    private var pendingIntent: Intent? = null
    
    // Track the current theme to detect changes
    private var currentNightMode: Int = Configuration.UI_MODE_NIGHT_UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        Log.d("ThemeTest", "Is dark mode: ${currentNightMode == Configuration.UI_MODE_NIGHT_YES}")

        // Store the initial intent if it exists
        if (intent?.action == DeepLinkHandler.ACTION_OPEN_TASK) {
            pendingIntent = intent
        }

        setContent {
            DoTrackApp(pendingIntent)
        }
        
        // Switch app icon on first launch with a delay
        Handler(Looper.getMainLooper()).postDelayed({
            switchAppIcon(this)
        }, 1000) // 1 second delay on first launch
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        
        val newNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        
        // Only switch icon if the night mode actually changed
        if (newNightMode != currentNightMode) {
            Log.d("ThemeTest", "Theme changed from $currentNightMode to $newNightMode")
            currentNightMode = newNightMode
            
            // Switch app icon with a delay to avoid conflicts during configuration change
            Handler(Looper.getMainLooper()).postDelayed({
                switchAppIcon(this)
            }, 1500) // 1.5 second delay during configuration change
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // Update the activity's intent
        setIntent(intent)

        // Handle the new intent in the UI
        if (intent.action == DeepLinkHandler.ACTION_OPEN_TASK) {
            setContent {
                DoTrackApp(intent)
            }
        }
    }
}

@Composable
fun DoTrackApp(deepLinkIntent: Intent? = null) {
    DoTrackTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val showBottomBar = currentDestination?.route in bottomNavDestinations.map { it.route }

        // Handle deep link intent
        if (deepLinkIntent != null) {
            // Use LaunchedEffect to handle the deep link after the NavHost is set up
            LaunchedEffect(deepLinkIntent) {
                DeepLinkHandler.handleIntent(deepLinkIntent, navController)
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
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

@Preview(showBackground = true)
@Composable
fun DoTrackAppPreview() {
    DoTrackTheme {
        // Use a fake nav controller for preview
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                DoTrackBottomNavigation(
                    navController = navController,
                    currentDestination = null // or a mock destination
                )
            }
        ) { innerPadding ->
            // Simple placeholder for preview
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Preview Content")
            }
        }
    }
}



