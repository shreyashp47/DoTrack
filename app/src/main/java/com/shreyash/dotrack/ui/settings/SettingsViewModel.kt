package com.shreyash.dotrack.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetHighPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetLowPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetMediumPriorityColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetWallpaperColorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val setAutoWallpaperEnabledUseCase: SetAutoWallpaperEnabledUseCase,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val setWallpaperColorUseCase: SetWallpaperColorUseCase,
    private val getHighPriorityColorUseCase: GetHighPriorityColorUseCase,
    private val setHighPriorityColorUseCase: SetHighPriorityColorUseCase,
    private val getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase,
    private val setMediumPriorityColorUseCase: SetMediumPriorityColorUseCase,
    private val getLowPriorityColorUseCase: GetLowPriorityColorUseCase,
    private val setLowPriorityColorUseCase: SetLowPriorityColorUseCase
) : ViewModel() {

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
            initialValue = "#3A0CA3" // Default blue color
        )

    /**
     * High priority color state
     */
    val highPriorityColor: StateFlow<String> = getHighPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "#FFE7EA" // Default high priority color
        )

    /**
     * Medium priority color state
     */
    val mediumPriorityColor: StateFlow<String> = getMediumPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "#FFF5D6" // Default medium priority color
        )

    /**
     * Low priority color state
     */
    val lowPriorityColor: StateFlow<String> = getLowPriorityColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "#DFF5E0" // Default low priority color
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
            setWallpaperColorUseCase(colorHex)
        }
    }

    /**
     * Set priority color
     */
    fun setPriorityColor(priority: Priority, color: Color) {
        val colorHex = "#" + Integer.toHexString(color.toArgb()).substring(2)
        viewModelScope.launch {
            when (priority) {
                Priority.HIGH -> setHighPriorityColorUseCase(colorHex)
                Priority.MEDIUM -> setMediumPriorityColorUseCase(colorHex)
                Priority.LOW -> setLowPriorityColorUseCase(colorHex)
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
            selectedColor = Color(0xFF3A0CA3) // Default if parsing fails
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
}

/**
 * Color picker mode enum
 */
enum class ColorPickerMode {
    WALLPAPER,
    HIGH_PRIORITY,
    MEDIUM_PRIORITY,
    LOW_PRIORITY
}