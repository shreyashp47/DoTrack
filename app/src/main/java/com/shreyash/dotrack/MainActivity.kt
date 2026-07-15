package com.shreyash.dotrack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shreyash.dotrack.core.ui.theme.DoTrackTheme
import com.shreyash.dotrack.navigation.DeepLinkHandler
import com.shreyash.dotrack.navigation.DoTrackBottomNavigation
import com.shreyash.dotrack.navigation.DoTrackNavHost
import com.shreyash.dotrack.navigation.bottomNavDestinations
import com.shreyash.dotrack.ui.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var deepLinkCounter by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoTrackApp(
                deepLinkTrigger = deepLinkCounter,
                getDeepLinkIntent = {
                    if (intent?.action == DeepLinkHandler.ACTION_OPEN_TASK) intent else null
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.action == DeepLinkHandler.ACTION_OPEN_TASK) {
            deepLinkCounter++
        }
    }
}

@Composable
fun DoTrackApp(
    deepLinkTrigger: Int = 0,
    getDeepLinkIntent: () -> Intent? = { null }
) {
    val deepLinkIntent = remember(deepLinkTrigger) { getDeepLinkIntent() }

    val themeViewModel: ThemeViewModel = if (LocalInspectionMode.current) {
        hiltViewModel()
    } else {
        hiltViewModel()
    }
    val darkModePref by themeViewModel.darkMode.collectAsState()
    val isDark = when (darkModePref) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    DoTrackTheme(darkTheme = isDark) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val showBottomBar = currentDestination?.route in bottomNavDestinations.map { it.route }

        if (deepLinkIntent != null) {
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



