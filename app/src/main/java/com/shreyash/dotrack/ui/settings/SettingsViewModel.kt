package com.shreyash.dotrack.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.core.ui.theme.DEFAULT_HIGH_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_LOW_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_MEDIUM_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_TOP_COLOR
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSecondaryWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetSecondaryWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val setAutoWallpaperEnabledUseCase: SetAutoWallpaperEnabledUseCase,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val setWallpaperColorUseCase: SetWallpaperColorUseCase,
    private val setSecondaryWallpaperColorUseCase: SetSecondaryWallpaperColorUseCase,
    private val getSecondaryWallpaperColorUseCase: GetSecondaryWallpaperColorUseCase,
    private val getHighPriorityColorUseCase: GetHighPriorityColorUseCase,
    private val setHighPriorityColorUseCase: SetHighPriorityColorUseCase,
    private val getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase,
    private val setMediumPriorityColorUseCase: SetMediumPriorityColorUseCase,
    private val getLowPriorityColorUseCase: GetLowPriorityColorUseCase,
    private val setLowPriorityColorUseCase: SetLowPriorityColorUseCase,
    private val wallpaperGenerator: WallpaperGenerator,
    private val getTasksUseCase: GetTasksUseCase,
) : ViewModel() {


    private val TAG = "SettingsViewModel"

    /**
     * Notification permission state
     */
    var notificationPermissionState by mutableStateOf(checkNotificationPermission())
        private set

    /**
     * Auto wallpaper enabled state
     */
    val autoWallpaperEnabled: StateFlow<Boolean> = getAutoWallpaperEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Wallpaper color state
     */
    val wallpaperColor: StateFlow<String> = getWallpaperColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DEFAULT_TOP_COLOR // Default blue color
        )

    val wallpaperSecondaryColor: StateFlow<String> = getSecondaryWallpaperColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DEFAULT_TOP_COLOR // Default blue color
        )

    /**
     * High priority color state
     */
    val highPriorityColor: StateFlow<String> = getHighPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DEFAULT_HIGH_PRIORITY_COLOR // Default high priority color
        )

    /**
     * Medium priority color state
     */
    val mediumPriorityColor: StateFlow<String> = getMediumPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DEFAULT_MEDIUM_PRIORITY_COLOR // Default medium priority color
        )

    /**
     * Low priority color state
     */
    val lowPriorityColor: StateFlow<String> = getLowPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DEFAULT_LOW_PRIORITY_COLOR // Default low priority color
        )

    /**
     * Show color picker dialog state
     */
    var showColorPickerDialog by mutableStateOf(false)
        private set

    /**
     * Current selected color in the color picker
     */
    var selectedColor by mutableStateOf(Color(0xFF3A0CA3))
        private set

    /**
     * Current color picker mode
     */
    var currentColorPickerMode by mutableStateOf(ColorPickerMode.WALLPAPER)
        private set

    /**
     * Set auto wallpaper enabled
     */
    fun setAutoWallpaperEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setAutoWallpaperEnabledUseCase(enabled)
        }
    }

    /**
     * Set wallpaper color
     */
    fun setWallpaperColor(color: Color) {
        val colorHex = "#" + Integer.toHexString(color.toArgb()).substring(2)
        viewModelScope.launch {
            val result = setWallpaperColorUseCase(colorHex)
            if (result.isSuccess()) {
                updateWallpaper()
            }
        }
    }

    fun setSecondaryWallpaperColor(color: Color) {
        val colorHex = "#" + Integer.toHexString(color.toArgb()).substring(2)
        viewModelScope.launch {
            val result = setSecondaryWallpaperColorUseCase(colorHex)
            if (result.isSuccess()) {
                updateWallpaper()
            }

        }
    }

    /**
     * Set priority color
     */
    fun setPriorityColor(priority: Priority, color: Color) {
        val colorHex = "#" + Integer.toHexString(color.toArgb()).substring(2)
        viewModelScope.launch {
            val result = when (priority) {
                Priority.HIGH -> setHighPriorityColorUseCase(colorHex)
                Priority.MEDIUM -> setMediumPriorityColorUseCase(colorHex)
                Priority.LOW -> setLowPriorityColorUseCase(colorHex)
            }
            if (result.isSuccess()) {
                updateWallpaper()
            }
        }
    }

    /**
     * Update the selected color
     */
    fun updateSelectedColor(color: Color) {
        selectedColor = color
    }

    /**
     * Show color picker dialog for wallpaper
     */
    fun showWallpaperColorPicker() {
        currentColorPickerMode = ColorPickerMode.WALLPAPER
        // Initialize the color picker with the current wallpaper color
        val currentColorHex = wallpaperColor.value
        try {
            selectedColor = Color(android.graphics.Color.parseColor(currentColorHex))
        } catch (e: Exception) {
            selectedColor = Color(0xFF1A2980) // Default if parsing fails
        }
        showColorPickerDialog = true
    }

    fun showSecondaryWallpaperColorPicker() {
        currentColorPickerMode = ColorPickerMode.SECONDARY_WALLPAPER
        // Initialize the color picker with the current wallpaper color
        val currentColorHex = wallpaperSecondaryColor.value
        try {
            selectedColor = Color(android.graphics.Color.parseColor(currentColorHex))
        } catch (e: Exception) {
            selectedColor = Color(0xFF26D0CE) // Default if parsing fails
        }
        showColorPickerDialog = true
    }

    /**
     * Show color picker dialog for priority
     */
    fun showPriorityColorPicker(priority: Priority) {
        currentColorPickerMode = when (priority) {
            Priority.HIGH -> ColorPickerMode.HIGH_PRIORITY
            Priority.MEDIUM -> ColorPickerMode.MEDIUM_PRIORITY
            Priority.LOW -> ColorPickerMode.LOW_PRIORITY
        }

        // Initialize the color picker with the current priority color
        val currentColorHex = when (priority) {
            Priority.HIGH -> highPriorityColor.value
            Priority.MEDIUM -> mediumPriorityColor.value
            Priority.LOW -> lowPriorityColor.value
        }

        try {
            selectedColor = Color(android.graphics.Color.parseColor(currentColorHex))
        } catch (e: Exception) {
            selectedColor = when (priority) {
                Priority.HIGH -> Color(0xFFFFE7EA)
                Priority.MEDIUM -> Color(0xFFFFF5D6)
                Priority.LOW -> Color(0xFFDFF5E0)
            }
        }
        showColorPickerDialog = true
    }

    /**
     * Apply the selected color based on the current mode
     */
    fun applySelectedColor() {
        when (currentColorPickerMode) {
            ColorPickerMode.WALLPAPER -> setWallpaperColor(selectedColor)
            ColorPickerMode.SECONDARY_WALLPAPER -> setSecondaryWallpaperColor(selectedColor)
            ColorPickerMode.HIGH_PRIORITY -> setPriorityColor(Priority.HIGH, selectedColor)
            ColorPickerMode.MEDIUM_PRIORITY -> setPriorityColor(Priority.MEDIUM, selectedColor)
            ColorPickerMode.LOW_PRIORITY -> setPriorityColor(Priority.LOW, selectedColor)
        }
        hideColorPicker()
    }

    /**
     * Hide color picker dialog
     */
    fun hideColorPicker() {
        showColorPickerDialog = false
    }

    /**
     * Check if notification permission is granted
     */
    fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For versions below Android 13, notification permissions were granted by default
            true
        }
    }

    /**
     * Update notification permission state
     */
    fun updateNotificationPermissionState() {
        notificationPermissionState = checkNotificationPermission()
    }

    private suspend fun updateWallpaper() {
        val tasksResult = getTasksUseCase().first()
        val autoWallpaperEnabled = getAutoWallpaperEnabledUseCase().first()
        if (tasksResult.isSuccess() && autoWallpaperEnabled) {
            val tasks = tasksResult.getOrNull() ?: emptyList()
            val wallpaperResult = wallpaperGenerator.generateAndSetWallpaper(tasks)

        }
    }
}

/**
 * Color picker mode enum
 */
enum class ColorPickerMode {
    WALLPAPER,
    SECONDARY_WALLPAPER,
    HIGH_PRIORITY,
    MEDIUM_PRIORITY,
    LOW_PRIORITY
}