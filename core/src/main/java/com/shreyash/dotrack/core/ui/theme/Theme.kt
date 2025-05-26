package com.shreyash.dotrack.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    errorContainer = CardColorHighPriorityDark,
    tertiaryContainer = CardColorMediumPriorityDark,
    primaryContainer = CardColorLowPriorityDark,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    errorContainer = CardColorHighPriorityLight,
    tertiaryContainer = CardColorMediumPriorityLight,
    primaryContainer = CardColorLowPriorityLight,
)

@Composable
fun DoTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    // Set the priority card colors based on the current theme
    val highPriorityCardColor = if (darkTheme) CardColorHighPriorityDark else CardColorHighPriorityLight
    val mediumPriorityCardColor = if (darkTheme) CardColorMediumPriorityDark else CardColorMediumPriorityLight
    val lowPriorityCardColor = if (darkTheme) CardColorLowPriorityDark else CardColorLowPriorityLight
    
    // Update the global color variables
    SideEffect {
        CardColorHighPriority = highPriorityCardColor
        CardColorMediumPriority = mediumPriorityCardColor
        CardColorLowPriority = lowPriorityCardColor
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DoTrackThemePreview() {
    DoTrackTheme {
        Surface(
            modifier = Modifier.size(100.dp, 100.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text("DoTrack Theme Preview")
            }
        }
    }
}