package com.shreyash.dotrack.widget

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shreyash.dotrack.data.local.TaskDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TaskWidgetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getString("taskId") ?: return Result.failure()
        val isCompleted = inputData.getBoolean("isCompleted", false)
        return try {
            val database = TaskDatabase.getInstance(applicationContext)
            val taskDao = database.taskDao()
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
