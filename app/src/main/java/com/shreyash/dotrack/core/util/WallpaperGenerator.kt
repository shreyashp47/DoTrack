package com.shreyash.dotrack.core.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.preferences.GetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetWallpaperColorUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val getHighPriorityColorUseCase: GetHighPriorityColorUseCase,
    private val getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase,
    private val getLowPriorityColorUseCase: GetLowPriorityColorUseCase
) {

    private val wallpaperManager = WallpaperManager.getInstance(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    // Default end color for gradient
    private val DEFAULT_END_COLOR = "#ffffff"

    /**
     * Generate a bitmap from the task list and set it as the wallpaper
     */
    suspend fun generateAndSetWallpaper(tasks: List<Task>): Result<Unit> {
        return try {
            // Get the user's preferred colors
            val startColorHex = getWallpaperColorUseCase().first()
            val highPriorityColorHex = getHighPriorityColorUseCase().first()
            val mediumPriorityColorHex = getMediumPriorityColorUseCase().first()
            val lowPriorityColorHex = getLowPriorityColorUseCase().first()

            val bitmap = generateTaskListBitmap(
                tasks, 
                startColorHex,
                highPriorityColorHex,
                mediumPriorityColorHex,
                lowPriorityColorHex
            )
            wallpaperManager.setBitmap(bitmap)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Generate a bitmap from the task list
     * @param startColorHex The hex color string for the gradient start color
     * @param highPriorityColorHex The hex color string for high priority tasks
     * @param mediumPriorityColorHex The hex color string for medium priority tasks
     * @param lowPriorityColorHex The hex color string for low priority tasks
     */
    private fun generateTaskListBitmap(
        tasks: List<Task>, 
        startColorHex: String,
        highPriorityColorHex: String,
        mediumPriorityColorHex: String,
        lowPriorityColorHex: String
    ): Bitmap {
        // Get screen dimensions
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        // Create bitmap and canvas
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw glossy gradient background with user's preferred color
        val gradient = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            Color.parseColor(startColorHex), // Start color from user preference
            Color.parseColor(DEFAULT_END_COLOR), // End color
            Shader.TileMode.CLAMP
        )
        val bgPaint = Paint().apply {
            shader = gradient
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Determine text color based on background color brightness
        val startColor = Color.parseColor(startColorHex)
        val isDarkColor = isDarkColor(startColor)

        // Setup text paints
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 35f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            setShadowLayer(1f, 1f, 1f, Color.WHITE)
        }

        val datePaint = Paint().apply {
            color = Color.BLACK
            textSize = 26f
            isAntiAlias = true
        }

        val footerPaint = Paint().apply {
            color = if (isDarkColor) Color.WHITE else Color.BLACK
            textSize = 30f
            isAntiAlias = true
            setShadowLayer(4f, 1f, 1f, if (isDarkColor) Color.BLACK else Color.WHITE)
        }

        // Layout configuration
        val topPadding = 300f
        val leftPadding = 80f
        val circleRadius = 20f
        val iconOffset = 40f
        val textLeftOffset = leftPadding + iconOffset + circleRadius + 20f
        val lineHeight = 100f

        var y = topPadding

        // Filter and sort pending tasks
        val pendingTasks = tasks.filter { !it.isCompleted }
            .sortedByDescending { it.priority.value }

        if (pendingTasks.isEmpty()) {
            val noTasksText = "No pending tasks 🎉"
            val textBounds = Rect()
            titlePaint.getTextBounds(noTasksText, 0, noTasksText.length, textBounds)
            canvas.drawText(
                noTasksText,
                (width - textBounds.width()) / 2f,
                height / 2f,
                titlePaint
            )
        } else {
            for (task in pendingTasks) {
                if (y > height - 200) break

                // Choose background color based on priority
                val bgColor = when (task.priority) {
                    Priority.HIGH -> Color.parseColor(highPriorityColorHex)
                    Priority.MEDIUM -> Color.parseColor(mediumPriorityColorHex)
                    Priority.LOW -> Color.parseColor(lowPriorityColorHex)
                }

                val bgPaint = Paint().apply {
                    color = bgColor
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                val itemHeight = 100f
                val cornerRadius = 20f

                // Draw rounded rectangle for background
                val rect = RectF(
                    leftPadding,
                    y,
                    width - leftPadding,
                    y + itemHeight
                )
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, bgPaint)

                // Draw title (left aligned)
                canvas.drawText(task.title, leftPadding + 20f, y + 65f, titlePaint)

                // Draw due date (right aligned)
                task.dueDate?.let {
                    val dueText = "Due: ${it.format(dateFormatter)}"
                    val dueWidth = datePaint.measureText(dueText)
                    canvas.drawText(
                        dueText,
                        width - dueWidth - leftPadding - 20f,
                        y + 65f,
                        datePaint
                    )
                }

                y += itemHeight + 30f // spacing between rows
            }
        }

        // Draw footer
        val footerText =
            "${pendingTasks.size} pending task${if (pendingTasks.size != 1) "s" else ""} • Updated on ${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
            }"
        canvas.drawText(footerText, leftPadding, y + 40f, footerPaint)

        return bitmap
    }

    /**
     * Determine if a color is dark (to choose appropriate text color)
     */
    private fun isDarkColor(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}
