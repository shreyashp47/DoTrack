# Data Module Consumer ProGuard Rules

# Keep all data entities
-keep class com.shreyash.dotrack.data.local.entity.** { *; }

# Keep all DAOs
-keep interface com.shreyash.dotrack.data.local.dao.** { *; }

# Keep database classes
-keep class com.shreyash.dotrack.data.local.database.** { *; }

# Keep repository implementations
-keep class com.shreyash.dotrack.data.repository.** { *; }

# Keep data sources
-keep class com.shreyash.dotrack.data.local.** { *; }
-keep class com.shreyash.dotrack.data.remote.** { *; }

# Keep mappers
-keep class com.shreyash.dotrack.data.mapper.** { *; }

# Room specific rules
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Keep Room generated classes
-keep class **_Impl { *; }
-keep class **_Impl$* { *; }

# DataStore rules
-keep class androidx.datastore.** { *; }

# Retrofit and Moshi rules
-keep class retrofit2.** { *; }
-keep class com.squareup.moshi.** { *; }

# Keep classes annotated with Hilt
-keep @javax.inject.Inject class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }