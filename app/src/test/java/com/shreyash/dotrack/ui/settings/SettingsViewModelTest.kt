package com.shreyash.dotrack.ui.settings

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.shreyash.dotrack.core.ui.theme.DEFAULT_HIGH_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_LOW_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_MEDIUM_PRIORITY_COLOR
import com.shreyash.dotrack.core.ui.theme.DEFAULT_TOP_COLOR
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
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
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private lateinit var context: Context
    private lateinit var getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase
    private lateinit var setAutoWallpaperEnabledUseCase: SetAutoWallpaperEnabledUseCase
    private lateinit var getWallpaperColorUseCase: GetWallpaperColorUseCase
    private lateinit var setWallpaperColorUseCase: SetWallpaperColorUseCase
    private lateinit var getSecondaryWallpaperColorUseCase: GetSecondaryWallpaperColorUseCase
    private lateinit var setSecondaryWallpaperColorUseCase: SetSecondaryWallpaperColorUseCase
    private lateinit var getHighPriorityColorUseCase: GetHighPriorityColorUseCase
    private lateinit var setHighPriorityColorUseCase: SetHighPriorityColorUseCase
    private lateinit var getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase
    private lateinit var setMediumPriorityColorUseCase: SetMediumPriorityColorUseCase
    private lateinit var getLowPriorityColorUseCase: GetLowPriorityColorUseCase
    private lateinit var setLowPriorityColorUseCase: SetLowPriorityColorUseCase
    private lateinit var wallpaperGenerator: WallpaperGenerator
    private lateinit var getTasksUseCase: GetTasksUseCase

    // Class under test
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Initialize mocks
        context = mockk(relaxed = true)
        getAutoWallpaperEnabledUseCase = mockk()
        setAutoWallpaperEnabledUseCase = mockk(relaxed = true)
        getWallpaperColorUseCase = mockk()
        setWallpaperColorUseCase = mockk()
        getSecondaryWallpaperColorUseCase = mockk()
        setSecondaryWallpaperColorUseCase = mockk()
        getHighPriorityColorUseCase = mockk()
        setHighPriorityColorUseCase = mockk()
        getMediumPriorityColorUseCase = mockk()
        setMediumPriorityColorUseCase = mockk()
        getLowPriorityColorUseCase = mockk()
        setLowPriorityColorUseCase = mockk()
        wallpaperGenerator = mockk(relaxed = true)
        getTasksUseCase = mockk()

        // Default mock behavior
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(false)
        every { getWallpaperColorUseCase() } returns flowOf(DEFAULT_TOP_COLOR)
        every { getSecondaryWallpaperColorUseCase() } returns flowOf(DEFAULT_TOP_COLOR)
        every { getHighPriorityColorUseCase() } returns flowOf(DEFAULT_HIGH_PRIORITY_COLOR)
        every { getMediumPriorityColorUseCase() } returns flowOf(DEFAULT_MEDIUM_PRIORITY_COLOR)
        every { getLowPriorityColorUseCase() } returns flowOf(DEFAULT_LOW_PRIORITY_COLOR)

        // Initialize the ViewModel
        viewModel = SettingsViewModel(
            context = context,
            getAutoWallpaperEnabledUseCase = getAutoWallpaperEnabledUseCase,
            setAutoWallpaperEnabledUseCase = setAutoWallpaperEnabledUseCase,
            getWallpaperColorUseCase = getWallpaperColorUseCase,
            setWallpaperColorUseCase = setWallpaperColorUseCase,
            getSecondaryWallpaperColorUseCase = getSecondaryWallpaperColorUseCase,
            setSecondaryWallpaperColorUseCase = setSecondaryWallpaperColorUseCase,
            getHighPriorityColorUseCase = getHighPriorityColorUseCase,
            setHighPriorityColorUseCase = setHighPriorityColorUseCase,
            getMediumPriorityColorUseCase = getMediumPriorityColorUseCase,
            setMediumPriorityColorUseCase = setMediumPriorityColorUseCase,
            getLowPriorityColorUseCase = getLowPriorityColorUseCase,
            setLowPriorityColorUseCase = setLowPriorityColorUseCase,
            wallpaperGenerator = wallpaperGenerator,
            getTasksUseCase = getTasksUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `test auto wallpaper toggle functionality`() = runTest {
        // Given
        coEvery { setAutoWallpaperEnabledUseCase(any()) } returns Result.Success(Unit)

        // When
        viewModel.setAutoWallpaperEnabled(true)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setAutoWallpaperEnabledUseCase(true) }
    }


    @Test
    fun `test set HIGH priority color`() = runTest {
        // Given
        val testColor = Color(0xFF123456)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)

        coEvery { setHighPriorityColorUseCase(colorHex) } returns Result.Success(Unit)
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(emptyList<Task>()))

        // When
        viewModel.setPriorityColor(Priority.HIGH, testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setHighPriorityColorUseCase(colorHex) }
    }

    @Test
    fun `test set MEDIUM priority color`() = runTest {
        // Given
        val testColor = Color(0xFF789ABC)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)

        coEvery { setMediumPriorityColorUseCase(colorHex) } returns Result.Success(Unit)
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(emptyList<Task>()))

        // When
        viewModel.setPriorityColor(Priority.MEDIUM, testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setMediumPriorityColorUseCase(colorHex) }
    }

    @Test
    fun `test set LOW priority color`() = runTest {
        // Given
        val testColor = Color(0xFFDEF012)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)

        coEvery { setLowPriorityColorUseCase(colorHex) } returns Result.Success(Unit)
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(emptyList<Task>()))

        // When
        viewModel.setPriorityColor(Priority.LOW, testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setLowPriorityColorUseCase(colorHex) }
    }

    @Test
    fun `test wallpaper updates on priority color change success`() = runTest {
        // Given
        val testColor = Color(0xFF123456)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)

        coEvery { setHighPriorityColorUseCase(colorHex) } returns Result.Success(Unit)
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(emptyList<Task>()))

        // When
        viewModel.setPriorityColor(Priority.HIGH, testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setHighPriorityColorUseCase(colorHex) }
        coVerify { wallpaperGenerator.generateAndSetWallpaper(any()) }
    }

    @Test
    fun `test no wallpaper update on priority color change failure`() = runTest {
        // Given
        val testColor = Color(0xFF123456)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)

        coEvery { setHighPriorityColorUseCase(colorHex) } returns Result.Error(Exception("Failed to set color"))
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(emptyList<Task>()))

        // When
        viewModel.setPriorityColor(Priority.HIGH, testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setHighPriorityColorUseCase(colorHex) }
        coVerify(exactly = 0) { wallpaperGenerator.generateAndSetWallpaper(any()) }
    }

    @Test
    fun `test color picker dialog state`() {
        // Given - initial state
        assertFalse(viewModel.showColorPickerDialog)

        // When - show color picker
        viewModel.showWallpaperColorPicker()

        // Then - dialog should be visible
        assertTrue(viewModel.showColorPickerDialog)
        assertEquals(ColorPickerMode.WALLPAPER, viewModel.currentColorPickerMode)

        // When - hide color picker
        viewModel.hideColorPicker()

        // Then - dialog should be hidden
        assertFalse(viewModel.showColorPickerDialog)
    }


    @Test
    fun `test color parsing exception handling`() {
        // Given - invalid color hex
        val invalidColorHex = "invalid_color"
        every { getWallpaperColorUseCase() } returns flowOf(invalidColorHex)

        // When - recreate viewModel to trigger the flow collection with the invalid color
        viewModel = SettingsViewModel(
            context = context,
            getAutoWallpaperEnabledUseCase = getAutoWallpaperEnabledUseCase,
            setAutoWallpaperEnabledUseCase = setAutoWallpaperEnabledUseCase,
            getWallpaperColorUseCase = getWallpaperColorUseCase,
            setWallpaperColorUseCase = setWallpaperColorUseCase,
            getSecondaryWallpaperColorUseCase = getSecondaryWallpaperColorUseCase,
            setSecondaryWallpaperColorUseCase = setSecondaryWallpaperColorUseCase,
            getHighPriorityColorUseCase = getHighPriorityColorUseCase,
            setHighPriorityColorUseCase = setHighPriorityColorUseCase,
            getMediumPriorityColorUseCase = getMediumPriorityColorUseCase,
            setMediumPriorityColorUseCase = setMediumPriorityColorUseCase,
            getLowPriorityColorUseCase = getLowPriorityColorUseCase,
            setLowPriorityColorUseCase = setLowPriorityColorUseCase,
            wallpaperGenerator = wallpaperGenerator,
            getTasksUseCase = getTasksUseCase
        )

        // Then - show color picker should not crash
        viewModel.showWallpaperColorPicker()

        // Default color should be used
        assertEquals(Color(0xFF1A2980), viewModel.selectedColor)
    }

    @Test
    fun `test wallpaper update after color change`() = runTest {
        // Given
        val testColor = Color(0xFF123456)
        val colorHex = "#" + Integer.toHexString(testColor.toArgb()).substring(2)
        val tasks = listOf(mockk<Task>())

        coEvery { setWallpaperColorUseCase(colorHex) } returns Result.Success(Unit)
        every { getAutoWallpaperEnabledUseCase() } returns flowOf(true)
        every { getTasksUseCase() } returns flowOf(Result.Success(tasks))

        // When
        viewModel.setWallpaperColor(testColor)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { setWallpaperColorUseCase(colorHex) }
        coVerify { wallpaperGenerator.generateAndSetWallpaper(tasks) }
    }

    @Test
    fun `test color picker mode selection`() {
        // Test for each priority
        viewModel.showPriorityColorPicker(Priority.HIGH)
        assertEquals(ColorPickerMode.HIGH_PRIORITY, viewModel.currentColorPickerMode)

        viewModel.showPriorityColorPicker(Priority.MEDIUM)
        assertEquals(ColorPickerMode.MEDIUM_PRIORITY, viewModel.currentColorPickerMode)

        viewModel.showPriorityColorPicker(Priority.LOW)
        assertEquals(ColorPickerMode.LOW_PRIORITY, viewModel.currentColorPickerMode)

        // Test for wallpaper
        viewModel.showWallpaperColorPicker()
        assertEquals(ColorPickerMode.WALLPAPER, viewModel.currentColorPickerMode)

        // Test for secondary wallpaper
        viewModel.showSecondaryWallpaperColorPicker()
        assertEquals(ColorPickerMode.SECONDARY_WALLPAPER, viewModel.currentColorPickerMode)
    }
}