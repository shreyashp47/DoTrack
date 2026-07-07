package com.shreyash.dotrack.widget

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shreyash.dotrack.data.local.dao.TaskDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TaskWidgetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskDao: TaskDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getString("taskId") ?: return Result.failure()
        val isCompleted = inputData.getBoolean("isCompleted", false)
        return try {
            if (isCompleted) {
                taskDao.completeTask(taskId)
            } else {
                taskDao.uncompleteTask(taskId)
            }
            val action = if (isCompleted) "completed" else "uncompleted"
            TaskWidgetUpdater.updateTaskWidgets(applicationContext)
            Log.d("TaskWidgetWorker", "Task $taskId $action")
            Result.success()
        } catch (e: Exception) {
            Log.e("TaskWidgetWorker", "Error updating task completion", e)
            Result.failure()
        }
    }
}
