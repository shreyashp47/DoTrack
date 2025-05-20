package com.shreyash.dotrack.core.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.RequiresApi
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wallpaperManager = WallpaperManager.getInstance(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    /**
     * Generate a bitmap from the task list and set it as the wallpaper
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun generateAndSetWallpaper(tasks: List<Task>): Result<Unit> {
        return try {
            val bitmap = generateTaskListBitmap(tasks)
            wallpaperManager.setBitmap(bitmap)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Generate a bitmap from the task list
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateTaskListBitmap(tasks: List<Task>): Bitmap {
        // Get screen dimensions
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        // Create bitmap and canvas
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fill background
        canvas.drawColor(Color.BLACK)

        // Setup text paints
        val headerPaint = Paint().apply {
            color = Color.WHITE
            textSize = 64f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 48f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val descriptionPaint = Paint().apply {
            color = Color.LTGRAY
            textSize = 36f
            isAntiAlias = true
        }

        val datePaint = Paint().apply {
            color = Color.GRAY
            textSize = 32f
            isAntiAlias = true
        }

        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 30f
            isAntiAlias = true
        }

        // Layout configuration
        val topPadding = 400f
        val leftPadding = 100f
        val circleRadius = 20f
        val iconOffset = 40f
        val textLeftOffset = leftPadding + iconOffset + circleRadius + 20f
        val lineHeight = 120f

        // Draw header
//        val header = "DoTrack To-Do List"
//        val headerBounds = Rect()
//        headerPaint.getTextBounds(header, 0, header.length, headerBounds)
//        canvas.drawText(
//            header,
//            (width - headerBounds.width()) / 2f,
//            topPadding,
//            headerPaint
//        )

        // Start drawing tasks
        var y = topPadding + 80f

        // Filter pending tasks
        val pendingTasks = tasks.filter { !it.isCompleted }
            .sortedByDescending { it.priority.value }

        if (pendingTasks.isEmpty()) {
            val noTasksText = "No pending tasks! 🎉"
            val textBounds = Rect()
            headerPaint.getTextBounds(noTasksText, 0, noTasksText.length, textBounds)

            canvas.drawText(
                noTasksText,
                (width - textBounds.width()) / 2f,
                height / 2f,
                headerPaint
            )
        } else {
            for (task in pendingTasks) {
                if (y > height - 200) break

                // Draw priority circle
                val priorityColor = when (task.priority) {
                    Priority.HIGH -> Color.RED
                    Priority.MEDIUM -> Color.YELLOW
                    Priority.LOW -> Color.CYAN
                }

                val priorityPaint = Paint().apply {
                    color = priorityColor
                    style = Paint.Style.FILL
                }

                canvas.drawCircle(leftPadding, y + 20f, circleRadius, priorityPaint)

                // Draw title
                canvas.drawText(task.title, textLeftOffset, y + 20f, titlePaint)

                // Draw description
//                if (task.description.isNotBlank()) {
//                    val desc = task.description.take(40) + if (task.description.length > 40) "..." else ""
//                    canvas.drawText(desc, textLeftOffset, y + 70f, descriptionPaint)
//                }

                // Draw due date
                task.dueDate?.let {
                    val dueText = "Due: ${it.format(dateFormatter)}"
                    val dueWidth = datePaint.measureText(dueText)
                    canvas.drawText(dueText, width - dueWidth - leftPadding, y + 20f, datePaint)
                }

                y += lineHeight
            }
        }

        // Draw footer
        val footerText = "${pendingTasks.size} pending task${if (pendingTasks.size != 1) "s" else ""} • Updated: ${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
        }"

        canvas.drawText(footerText, leftPadding, height - 100f, footerPaint)

        return bitmap
    }
}
