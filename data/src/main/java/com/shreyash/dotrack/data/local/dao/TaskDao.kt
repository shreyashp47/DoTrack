package com.shreyash.dotrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shreyash.dotrack.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("""SELECT * FROM tasks ORDER BY isCompleted ASC, 
        CASE priority 
            WHEN 'HIGH' THEN 1 
            WHEN 'MEDIUM' THEN 2 
            WHEN 'LOW' THEN 3 
            ELSE 4 
        END ASC, 
        createdAt DESC """)
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("""
    SELECT * FROM tasks 
    WHERE isCompleted = 0
    ORDER BY 
        CASE priority 
            WHEN 'HIGH' THEN 1 
            WHEN 'MEDIUM' THEN 2 
            WHEN 'LOW' THEN 3 
            ELSE 4 
        END ASC, 
        createdAt DESC
""")
    fun getPendingTasksSync(): List<TaskEntity>




    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: String): Flow<TaskEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteAllTask()
    
    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :id")
    suspend fun completeTask(id: String)
    
    @Query("UPDATE tasks SET isCompleted = 0 WHERE id = :id")
    suspend fun uncompleteTask(id: String)
    
    @Query("UPDATE tasks SET reminderEnabled = 0 WHERE id = :id")
    suspend fun disableReminder(id: String)
}