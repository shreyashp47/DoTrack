package com.shreyash.dotrack.core.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
// Light theme priority card colors
val CardColorHighPriorityLight = Color(0xFFFFEBEE) // Light red background
val CardColorMediumPriorityLight = Color(0xFFFFF3E0) // Light orange background
val CardColorLowPriorityLight = Color(0xFFE8F5E9) // Light green background

val CardColorHighPriorityDark = Color(0xCCAD1F2D)   // 80% opacity
val CardColorMediumPriorityDark = Color(0xCCB76E22)
val CardColorLowPriorityDark = Color(0xCC1E7F5C)



// Current theme priority card colors (default to light theme)
var CardColorHighPriority = CardColorHighPriorityLight
var CardColorMediumPriority = CardColorMediumPriorityLight
var CardColorLowPriority = CardColorLowPriorityLight

// Wallpaper colors (always light since wallpapers are typically light)
val WallpapersColorHighPriority = Color(0xFFFFE7EA) // Light red background
val WallpapersColorMediumPriority = Color(0xFFFFF5D6) // Light orange background
val WallpapersColorLowPriority = Color(0xFFDFF5E0) // Light green background

const val DEFAULT_TOP_COLOR = "#1A2980"
const val DEFAULT_BOTTOM_COLOR = "#26D0CE"


const val DEFAULT_HIGH_PRIORITY_COLOR = "#FF9A8B"     // Light coral (attention but friendly)
const val DEFAULT_MEDIUM_PRIORITY_COLOR = "#FFD580"  // Warm pastel yellow (subtle alert)
const val DEFAULT_LOW_PRIORITY_COLOR = "#CFFFB0"     // Pale mint green (soft, low urgency)