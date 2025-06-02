# Core Module Consumer ProGuard Rules

# Keep all public classes and methods in the core module
-keep public class com.shreyash.dotrack.core.** { *; }

# Keep UI theme classes
-keep class com.shreyash.dotrack.core.ui.theme.** { *; }

# Keep UI components
-keep class com.shreyash.dotrack.core.ui.components.** { *; }

# Keep utility classes
-keep class com.shreyash.dotrack.core.util.** { *; }

# Keep worker classes
-keep class com.shreyash.dotrack.core.worker.** { *; }

# Keep Compose-related classes
-keep @androidx.compose.runtime.Composable class * {
    *;
}

# Keep classes with @Composable methods
-keep class * {
    @androidx.compose.runtime.Composable *;
}