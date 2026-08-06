package com.shreyash.dotrack.ui.tasks

import android.content.Context
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.core.util.WallpaperGenerator
import com.shreyash.dotrack.domain.ReminderScheduler
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.model.Task
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSortDirectionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetSortOptionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetSortDirectionUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetSortOptionUseCase
import com.shreyash.dotrack.domain.usecase.task.CompleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTaskUseCase
import com.shreyash.dotrack.domain.usecase.task.DeleteTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.DisableReminderUseCase
import com.shreyash.dotrack.domain.usecase.task.GetTasksUseCase
import com.shreyash.dotrack.domain.usecase.task.UncompleteTaskUseCase
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelOrderingTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var context: Context
    private lateinit var tasksFlow: MutableStateFlow<Result<List<Task>>>
    private lateinit var getTasksUseCase: GetTasksUseCase
    private lateinit var completeTaskUseCase: CompleteTaskUseCase
    private lateinit var uncompleteTaskUseCase: UncompleteTaskUseCase
    private lateinit var wallpaperGenerator: WallpaperGenerator
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var deleteTasksUseCase: DeleteTasksUseCase
    private lateinit var getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase
    private lateinit var disableReminderUseCase: DisableReminderUseCase
    private lateinit var reminderScheduler: ReminderScheduler
    private lateinit var getSortOptionUseCase: GetSortOptionUseCase
    private lateinit var setSortOptionUseCase: SetSortOptionUseCase
    private lateinit var getSortDirectionUseCase: GetSortDirectionUseCase
    private lateinit var setSortDirectionUseCase: SetSortDirectionUseCase

    private lateinit var viewModel: TasksViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher(testDispatcher.scheduler))
        context = mockk(relaxed = true)
        tasksFlow = MutableStateFlow(Result.Success(emptyList<Task>()))
        getTasksUseCase = mockk()
        completeTaskUseCase = mockk(relaxed = true)
        uncompleteTaskUseCase = mockk(relaxed = true)
        wallpaperGenerator = mockk(relaxed = true)
        deleteTaskUseCase = mockk(relaxed = true)
        deleteTasksUseCase = mockk(relaxed = true)
        getAutoWallpaperEnabledUseCase = mockk(relaxed = true)
        disableReminderUseCase = mockk(relaxed = true)
        reminderScheduler = mockk(relaxed = true)
        getSortOptionUseCase = mockk()
        setSortOptionUseCase = mockk(relaxed = true)
        getSortDirectionUseCase = mockk()
        setSortDirectionUseCase = mockk(relaxed = true)

        every { getSortOptionUseCase() } returns flowOf(SortOption.DUE_DATE)
        every { getSortDirectionUseCase() } returns flowOf(SortDirection.ASCENDING)
        every { getTasksUseCase() } returns tasksFlow

        viewModel = TasksViewModel(
            context = context,
            getTasksUseCase = getTasksUseCase,
            completeTaskUseCase = completeTaskUseCase,
            uncompleteTaskUseCase = uncompleteTaskUseCase,
            wallpaperGenerator = wallpaperGenerator,
            deleteTaskUseCase = deleteTaskUseCase,
            deleteTasksUseCase = deleteTasksUseCase,
            getAutoWallpaperEnabledUseCase = getAutoWallpaperEnabledUseCase,
            disableReminderUseCase = disableReminderUseCase,
            reminderScheduler = reminderScheduler,
            getSortOptionUseCase = getSortOptionUseCase,
            setSortOptionUseCase = setSortOptionUseCase,
            getSortDirectionUseCase = getSortDirectionUseCase,
            setSortDirectionUseCase = setSortDirectionUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private val now = LocalDateTime.of(2026, 8, 6, 12, 0)

    private fun task(
        id: String,
        priority: Priority,
        dueDate: LocalDateTime?,
        completed: Boolean = false,
    ) = Task(
        id = id,
        title = id,
        description = "",
        isCompleted = completed,
        dueDate = dueDate,
        priority = priority,
        reminderEnabled = false,
        createdAt = now,
        updatedAt = now,
        categoryId = null
    )

    private fun stubTasks(tasks: List<Task>) {
        tasksFlow.value = Result.Success(tasks)
    }

    /**
     * Subscribes to [TasksViewModel.sortedTasks] (WhileSubscribed), advances the
     * main test dispatcher until the combine emits, then returns the sorted task ids.
     */
    private suspend fun TestScope.sortedIds(
        statusFilter: Boolean? = null,
        priorityFilter: Priority? = null,
    ): List<String> {
        viewModel.setFilterStatus(statusFilter)
        viewModel.setFilterPriority(priorityFilter)
        val result = viewModel.sortedTasks.filterIsInstance<Result.Success<List<Task>>>().first()
        return result.data.map { it.id }
    }

    @Test
    fun `viewing all shows active tasks before completed within sort`() = runTest {
        // Completed task has the earliest due date - still must appear after all active tasks
        val activeLater = task("active-later", Priority.LOW, now.plusDays(5))
        val completedEarly = task("completed-early", Priority.HIGH, now.plusDays(1), completed = true)
        val activeSoon = task("active-soon", Priority.MEDIUM, now.plusDays(2))
        stubTasks(listOf(completedEarly, activeLater, activeSoon))

        assertEquals(listOf("active-soon", "active-later", "completed-early"), sortedIds())
    }

    @Test
    fun `viewing all keeps each group sorted by due date descending`() = runTest {
        val activeSoon = task("active-soon", Priority.MEDIUM, now.plusDays(2))
        val activeLater = task("active-later", Priority.LOW, now.plusDays(5))
        val completedEarly = task("completed-early", Priority.HIGH, now.plusDays(1), completed = true)
        stubTasks(listOf(completedEarly, activeLater, activeSoon))

        viewModel.setSortDirection(SortDirection.DESCENDING)

        assertEquals(listOf("active-later", "active-soon", "completed-early"), sortedIds())
    }

    @Test
    fun `active filter shows only incomplete tasks`() = runTest {
        val active = task("active", Priority.MEDIUM, now)
        val completed = task("completed", Priority.LOW, now, completed = true)
        stubTasks(listOf(completed, active))

        assertEquals(listOf("active"), sortedIds(statusFilter = false))
    }

    @Test
    fun `completed filter shows only completed tasks`() = runTest {
        val active = task("active", Priority.MEDIUM, now)
        val completed = task("completed", Priority.LOW, now, completed = true)
        stubTasks(listOf(completed, active))

        assertEquals(listOf("completed"), sortedIds(statusFilter = true))
    }

    @Test
    fun `priority filter composes with active-first ordering`() = runTest {
        val lowActive = task("low-active", Priority.LOW, now.plusDays(1))
        val highCompleted = task("high-completed", Priority.HIGH, now.plusDays(1), completed = true)
        val highActive = task("high-active", Priority.HIGH, now.plusDays(2))
        stubTasks(listOf(lowActive, highCompleted, highActive))

        assertEquals(listOf("high-active", "high-completed"), sortedIds(priorityFilter = Priority.HIGH))
    }
}