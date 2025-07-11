package com.shreyash.dotrack.widget

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.shreyash.dotrack.R
import com.shreyash.dotrack.TrackConstants
import com.shreyash.dotrack.data.local.TaskDatabase
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TaskWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TaskWidgetItemFactory(applicationContext)
    }
}

class TaskWidgetItemFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private var tasks: List<Task> = emptyList()

    override fun onCreate() {
        // No initialization needed
    }

    override fun onDataSetChanged() {
        val db = TaskDatabase.getInstance(context.applicationContext)
        val taskDao = db.taskDao()
        val taskEntities = taskDao.getPendingTasksSync()
        tasks = taskEntities.map { entity ->
            Task(
                id = entity.id,
                title = entity.title,
                isCompleted = entity.isCompleted,
                dueDate = entity.dueDate,
                priority = entity.priority,
                reminderEnabled = entity.reminderEnabled,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                description = entity.description
            )
        }
    }

    override fun onDestroy() {
        tasks = emptyList()
    }

    override fun getCount(): Int = tasks.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= tasks.size) {
            return RemoteViews(context.packageName, R.layout.task_widget_item)
        }

        val task = tasks[position]
        val remoteView = RemoteViews(context.packageName, R.layout.task_widget_item)

        // Set the task title and description
        remoteView.setTextViewText(R.id.widget_task_title, task.title.trim())
        if (!TextUtils.isEmpty(task.description)) {
            remoteView.setTextViewText(R.id.widget_task_description, task.description.trim())
            remoteView.setViewVisibility(R.id.widget_task_description, View.VISIBLE)
        }else
            remoteView.setViewVisibility(R.id.widget_task_description, View.GONE)

        // Set task checkbox image
        remoteView.setImageViewResource(
            R.id.widget_task_checkbox,
            if (task.isCompleted) R.drawable.checkbox_checked
            else R.drawable.checkbox_unchecked
        )

        if (task.dueDate != null) {
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())

            remoteView.setViewVisibility(R.id.widget_task_due, View.VISIBLE)
            remoteView.setTextViewText(R.id.widget_task_due, "Due: ${task.dueDate?.format(dateFormatter)}")
        } else {
            remoteView.setViewVisibility(R.id.widget_task_due, View.GONE)

        }

        when(task.priority){
            Priority.HIGH ->{
                remoteView.setTextViewText(R.id.widget_task_priority,"H")
            }
            Priority.MEDIUM ->{
                remoteView.setTextViewText(R.id.widget_task_priority,"M")
            }
            Priority.LOW ->{
                remoteView.setTextViewText(R.id.widget_task_priority,"L")
            }
        }

//        remoteView.setInt(R.id.task_background,
//                    "setBackgroundResource",
//                    R.drawable.task_background_transparent )
//        // Set background color based on priority
//        when (task.priority) {
//            Priority.HIGH -> {
//                rv.setInt(
//                    R.id.task_background,
//                    "setBackgroundResource",
//                    R.drawable.task_background_high
//                )
//                //rv.setImageViewResource(R.id.widget_priority_indicator, R.drawable.ic_priority_high)
//            }
//
//            Priority.MEDIUM -> {
//                rv.setInt(
//                    R.id.task_background,
//                    "setBackgroundResource",
//                    R.drawable.task_background_medium
//                )
//                //rv.setImageViewResource(R.id.widget_priority_indicator, R.drawable.ic_priority_medium)
//            }
//
//            Priority.LOW -> {
//                rv.setInt(
//                    R.id.task_background,
//                    "setBackgroundResource",
//                    R.drawable.task_background_low
//                )
//                //rv.setImageViewResource(R.id.widget_priority_indicator, R.drawable.ic_priority_low)
//            }
//        }

        // Set fill-in intent for item clicks
        val fillInIntent = Intent().apply {
            action = TrackConstants.ACTION_WIDGET_TASK_CLICK
            putExtra(TrackConstants.EXTRA_TASK_ID, task.id)
        }
        remoteView.setOnClickFillInIntent(R.id.task_background, fillInIntent)

        // Set checkbox click handler
        val checkIntent = Intent().apply {
            action = TrackConstants.ACTION_WIDGET_TASK_COMPLETE
            putExtra(TrackConstants.EXTRA_TASK_ID, task.id)
            putExtra(TrackConstants.EXTRA_TASK_COMPLETED, !task.isCompleted)
        }
        remoteView.setOnClickFillInIntent(R.id.widget_task_checkbox, checkIntent)

        return remoteView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    // Temporary method to provide sample data
    // In a real implementation, you would fetch data from your repository
    private fun getSampleTasks(): List<Task> {
        return listOf(
            Task(
                id = "1",
                title = "Complete project documentation",
                description = "Write up the final sections of the project docs",
                isCompleted = false,
                dueDate = LocalDateTime.now().plusDays(2),
                categoryId = 1,
                priority = Priority.MEDIUM,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Task(
                id = "2",
                title = "Review pull requests",
                description = "Check and approve team PRs",
                isCompleted = false,
                dueDate = LocalDateTime.now(),
                priority = Priority.LOW,
                categoryId = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Task(
                id = "3",
                title = "Team meeting",
                description = "Weekly sync with team",
                isCompleted = false,
                dueDate = LocalDateTime.now().plusDays(2),
                priority = Priority.HIGH,
                categoryId = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Task(
                id = "4",
                title = "Archive old documents",
                description = "Clean up project folders",
                isCompleted = true, // This should be filtered out in the widget
                dueDate = LocalDateTime.now().plusDays(5),
                priority = Priority.LOW,
                categoryId = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }
}