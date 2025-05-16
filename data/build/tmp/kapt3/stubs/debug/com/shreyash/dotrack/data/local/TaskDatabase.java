package com.shreyash.dotrack.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/shreyash/dotrack/data/local/TaskDatabase;", "Landroidx/room/RoomDatabase;", "()V", "taskDao", "Lcom/shreyash/dotrack/data/local/dao/TaskDao;", "data_debug"})
@androidx.room.Database(entities = {com.shreyash.dotrack.data.local.entity.TaskEntity.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.shreyash.dotrack.data.util.DateTimeConverters.class})
public abstract class TaskDatabase extends androidx.room.RoomDatabase {
    
    public TaskDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.shreyash.dotrack.data.local.dao.TaskDao taskDao();
}