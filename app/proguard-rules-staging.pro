# Staging ProGuard Rules - Less aggressive for testing

# Keep all class names for easier debugging
-keepnames class ** { *; }

# Keep all method names for easier debugging
-keepclassmembernames class * {
    *;
}

# Don't remove Log statements in staging
-keep class android.util.Log {
    *;
}

# Keep stack traces readable
-keepattributes SourceFile,LineNumberTable
-dontobfuscate

# Less aggressive optimizations for staging
-optimizationpasses 1
-dontoptimize