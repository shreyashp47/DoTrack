# DoTrack App ProGuard Rules (R8 full mode)
#
# Strategy: keep ONLY what reflection/codegen needs at runtime.
# All libraries below ship their own correct consumer rules inside their AARs
# and must NOT be blanket-kept here:
#   - Jetpack Compose / Navigation / Lifecycle / WorkManager
#   - Hilt / Dagger
#   - Room (runtime + compiler generated references)
#   - Kotlin Coroutines
# Everything else is shrunk and obfuscated by R8 full mode.

# DataStore Preferences vendored-protobuf generates reflective field access
# (e.g. MessageSchema looks up "preferences_" on the proto class). R8 strips
# those private fields unless the generated classes are kept.
-keep class androidx.datastore.preferences.PreferencesProto$** { *; }

# ================================
# Stack traces
# ================================
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*

# ================================
# @Keep annotation support
# ================================
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

# ================================
# Room
# (generated _Impl classes are covered by Room's own consumer rules;
#  keep only the database and entities that map to SQLite tables)
# ================================
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }

# ================================
# WorkManager
# Workers are instantiated via reflection with (Context, WorkerParameters)
# ================================
-keep class * extends androidx.work.ListenableWorker {
    <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ================================
# Hilt
# ViewModels are constructed reflectively by Hilt/SavedStateViewModelFactory
# ================================
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * {
    <init>(...);
}
-keep class dagger.hilt.android.internal.lifecycle.ViewComponentManager { *; }

# ================================
# Parcelable / Serializable support classes
# (system framework may marshal these across processes)
# ================================
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ================================
# Remove Logging in Release Builds
# ================================
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
