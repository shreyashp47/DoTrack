# Data Module Consumer ProGuard Rules
#
# Only Room requires keep rules here:
#  - @Entity classes are inspected reflectively by Room's TableInfo at runtime
#    (field names must survive), so they must not be shrunk or obfuscated.
#  - RoomDatabase subclasses (including the generated *_Impl) are loaded via
#    Class.forName, so their names and constructors must survive.
#
# DAO interfaces, repositories, mappers, and DataStore code are all referenced
# directly and are safely shrinkable/obfuscatable.
-keep @androidx.room.Entity class * { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
