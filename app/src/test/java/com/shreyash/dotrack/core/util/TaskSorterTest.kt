package com.shreyash.dotrack.core.util

import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.model.Task
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskSorterTest {

    private val now = LocalDateTime.of(2026, 8, 6, 12, 0)

    private fun task(
        id: String,
        title: String,
        priority: Priority,
        dueDate: LocalDateTime?,
        createdAt: LocalDateTime,
        completed: Boolean = false,
    ) = Task(
        id = id,
        title = title,
        description = "",
        isCompleted = completed,
        dueDate = dueDate,
        priority = priority,
        reminderEnabled = false,
        createdAt = createdAt,
        updatedAt = createdAt,
        categoryId = null
    )

    // t1: due today, created 3 days ago, LOW
    private val t1 = task("t1", "Alpha", Priority.LOW, now, now.minusDays(3))
    // t2: due tomorrow, created in 2 days, HIGH
    private val t2 = task("t2", "Beta", Priority.HIGH, now.plusDays(1), now.plusDays(2))
    // t3: no due date, created now, MEDIUM
    private val t3 = task("t3", "Gamma", Priority.MEDIUM, null, now)
    // t4: due day after tomorrow, created tomorrow, LOW
    private val t4 = task("t4", "apple first", Priority.LOW, now.plusDays(2), now.plusDays(1))

    private fun ids(tasks: List<Task>) = tasks.map { it.id }

    @Test
    fun `sorts by due date ascending with no-due tasks last`() {
        val result = TaskSorter.sort(
            listOf(t3, t1, t2, t4),
            SortOption.DUE_DATE,
            SortDirection.ASCENDING
        )
        assertEquals(listOf("t1", "t2", "t4", "t3"), ids(result))
    }

    @Test
    fun `sorts by due date descending with no-due tasks first`() {
        val result = TaskSorter.sort(
            listOf(t3, t4, t2, t1),
            SortOption.DUE_DATE,
            SortDirection.DESCENDING
        )
        assertEquals(listOf("t3", "t4", "t2", "t1"), ids(result))
    }

    @Test
    fun `sorts by priority descending`() {
        val result = TaskSorter.sort(
            listOf(t1, t3, t2),
            SortOption.PRIORITY,
            SortDirection.DESCENDING
        )
        assertEquals(listOf("t1", "t3", "t2"), ids(result))
    }

    @Test
    fun `sorts by priority ascending`() {
        val result = TaskSorter.sort(
            listOf(t1, t3, t2),
            SortOption.PRIORITY,
            SortDirection.ASCENDING
        )
        assertEquals(listOf("t2", "t3", "t1"), ids(result))
    }

    @Test
    fun `sorts by created date ascending`() {
        val result = TaskSorter.sort(
            listOf(t4, t1, t3, t2),
            SortOption.CREATED_DATE,
            SortDirection.ASCENDING
        )
        assertEquals(listOf("t1", "t3", "t4", "t2"), ids(result))
    }

    @Test
    fun `sorts by title case-insensitive ascending`() {
        val upper = task("u", "Alpha", Priority.LOW, null, now)
        val lower = task("a", "beta", Priority.LOW, null, now)
        val result = TaskSorter.sort(
            listOf(lower, upper),
            SortOption.TITLE,
            SortDirection.ASCENDING
        )
        assertEquals(listOf("u", "a"), ids(result))
    }

    @Test
    fun `sort does not mutate the input list`() {
        val original = listOf(t1, t2, t4)
        TaskSorter.sort(original, SortOption.TITLE, SortDirection.ASCENDING)
        assertEquals(original, listOf(t1, t2, t4))
    }
}