# Domain Module Consumer ProGuard Rules

# Keep all domain models
-keep class com.shreyash.dotrack.domain.model.** { *; }

# Keep all repositories interfaces
-keep interface com.shreyash.dotrack.domain.repository.** { *; }

# Keep all use cases
-keep class com.shreyash.dotrack.domain.usecase.** { *; }

# Keep enums
-keep enum com.shreyash.dotrack.domain.** { *; }

# Keep data classes with all their fields
-keepclassmembers class com.shreyash.dotrack.domain.model.** {
    *;
}

# Keep classes annotated with Hilt
-keep @javax.inject.Inject class * { *; }
-keep @dagger.Module class * { *; }