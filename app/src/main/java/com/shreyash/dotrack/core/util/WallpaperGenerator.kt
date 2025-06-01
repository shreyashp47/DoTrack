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
import androidx.core.graphics.ColorUtils
import com.shreyash.dotrack.core.ui.theme.DEFAULT_BOTTOM_COLOR
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.preferences.GetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSecondaryWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetWallpaperColorUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val getSecondaryWallpaperColorUseCase: GetSecondaryWallpaperColorUseCase,
    private val getHighPriorityColorUseCase: GetHighPriorityColorUseCase,
    private val getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase,
    private val getLowPriorityColorUseCase: GetLowPriorityColorUseCase
) {

    private val wallpaperManager = WallpaperManager.getInstance(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy  HH:mm")


    /**
     * Generate a bitmap from the task list and set it as the wallpaper
     */
    suspend fun generateAndSetWallpaper(tasks: List<Task>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Get the user's preferred colors
                val startColorHex = getWallpaperColorUseCase().first()
                val secondaryStartColorHex = getSecondaryWallpaperColorUseCase().first()
                val highPriorityColorHex = getHighPriorityColorUseCase().first()
                val mediumPriorityColorHex = getMediumPriorityColorUseCase().first()
                val lowPriorityColorHex = getLowPriorityColorUseCase().first()

                val bitmap = generateTaskListBitmap(
                    tasks, 
                    startColorHex,
                    secondaryStartColorHex,
                    highPriorityColorHex,
                    mediumPriorityColorHex,
                    lowPriorityColorHex
                )
                //only for system screen
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
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
        endColorHex: String,
        highPriorityColorHex: String,
        mediumPriorityColorHex: String,
        lowPriorityColorHex: String
    ): Bitmap {
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background gradient
        val gradient = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            Color.parseColor(startColorHex),
            Color.parseColor(endColorHex),
            Shader.TileMode.CLAMP
        )
        val bgPaint = Paint().apply {
            shader = gradient
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Determine appropriate text color
        val startColor = Color.parseColor(startColorHex)
        val isDark = isDarkColor(startColor)

        // Paints
        val titlePaint = Paint().apply {
            color = if (isDark) Color.WHITE else Color.BLACK
            textSize = 38f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val datePaint = Paint().apply {
            color = if (isDark) Color.WHITE else Color.BLACK
            textSize = 26f
            isAntiAlias = true
        }

        val footerPaint = Paint().apply {
            color = if (isDark) Color.WHITE else Color.BLACK
            textSize = 26f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }

        // Layout
        val topPadding = 300f
        val leftPadding = 80f
        val itemHeight = 100f
        val cornerRadius = 24f
        val spacing = 30f

        var y = topPadding

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

                val baseColor = when (task.priority) {
                    Priority.HIGH -> Color.parseColor(highPriorityColorHex)
                    Priority.MEDIUM -> Color.parseColor(mediumPriorityColorHex)
                    Priority.LOW -> Color.parseColor(lowPriorityColorHex)
                }

                val rect = RectF(
                    leftPadding,
                    y,
                    width - leftPadding,
                    y + itemHeight
                )

                // Glossy-style gradient on card
                val glossyGradient = LinearGradient(
                    rect.left, rect.top, rect.right, rect.bottom,
                    ColorUtils.blendARGB(baseColor, Color.WHITE, 0.2f),
                    baseColor,
                    Shader.TileMode.CLAMP
                )

                val taskPaint = Paint().apply {
                    shader = glossyGradient
                    isAntiAlias = true
                }

                // Draw glossy task card
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, taskPaint)

                // Task title
                canvas.drawText(task.title, rect.left + 24f, y + 65f, titlePaint)

                // Due date
                task.dueDate?.let {
                    val dueText = "Due: ${it.format(dateFormatter)}"
                    val dueWidth = datePaint.measureText(dueText)
                    canvas.drawText(
                        dueText,
                        rect.right - dueWidth - 24f,
                        y + 65f,
                        datePaint
                    )
                }

                y += itemHeight + spacing
            }
        }

        // Footer
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
