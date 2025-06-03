# DoTrack App ProGuard Rules

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep generic signatures for reflection
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# ================================
# Kotlin Coroutines
# ================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# ================================
# Jetpack Compose
# ================================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.animation.** { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * {
    *;
}

# Keep classes with @Composable methods
-keep class * {
    @androidx.compose.runtime.Composable *;
}

# ================================
# Hilt / Dagger
# ================================
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }

# Keep Hilt generated classes
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }
-keep class **_Provide* { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Keep classes annotated with Hilt annotations
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @javax.inject.Inject class * { *; }

# Keep Hilt entry points
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# ================================
# Room Database
# ================================
-keep class androidx.room.** { *; }
-keep class androidx.sqlite.** { *; }

# Keep Room entities and DAOs
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Keep Room generated classes
-keep class **_Impl { *; }
-keep class **_Impl$* { *; }

# ================================
# WorkManager
# ================================
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.ListenableWorker { *; }

# Keep Worker classes
-keep class com.shreyash.dotrack.core.worker.** { *; }

# ================================
# Navigation Component
# ================================
-keep class androidx.navigation.** { *; }
-keepclassmembers class * extends androidx.navigation.NavArgs {
    *;
}

# ================================
# Lifecycle Components
# ================================
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# ================================
# Color Picker Library
# ================================
-keep class com.github.skydoves.colorpicker.** { *; }

# ================================
# Gson (if used)
# ================================
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# ================================
# App-specific classes
# ================================

# Keep domain models
-keep class com.shreyash.dotrack.domain.model.** { *; }

# Keep data entities
-keep class com.shreyash.dotrack.data.local.entity.** { *; }

# Keep use cases
-keep class com.shreyash.dotrack.domain.usecase.** { *; }

# Keep repositories
-keep class com.shreyash.dotrack.domain.repository.** { *; }
-keep class com.shreyash.dotrack.data.repository.** { *; }

# Keep ViewModels
-keep class com.shreyash.dotrack.ui.**.ViewModel { *; }
-keep class com.shreyash.dotrack.ui.**.*ViewModel { *; }

# Keep Hilt modules
-keep class com.shreyash.dotrack.di.** { *; }

# Keep enum classes
-keep enum com.shreyash.dotrack.** { *; }

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ================================
# Android Components
# ================================

# Keep Activities, Services, Receivers
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep custom views
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# ================================
# Reflection and Native Methods
# ================================

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep classes with reflection
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# ================================
# Remove Logging in Release Builds
# ================================
# Remove all Log statements in release builds for better performance and security
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# ================================
# Optimization Settings
# ================================

# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Don't warn about missing classes (common with optional dependencies)
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe