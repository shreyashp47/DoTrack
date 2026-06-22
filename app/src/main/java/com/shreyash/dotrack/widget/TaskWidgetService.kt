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
import com.shreyash.dotrack.domain.model.Task
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

        when (task.priority) {
            com.shreyash.dotrack.domain.model.Priority.HIGH ->
                remoteView.setTextViewText(R.id.widget_task_priority, "H")
            com.shreyash.dotrack.domain.model.Priority.MEDIUM ->
                remoteView.setTextViewText(R.id.widget_task_priority, "M")
            com.shreyash.dotrack.domain.model.Priority.LOW ->
                remoteView.setTextViewText(R.id.widget_task_priority, "L")
        }

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
}