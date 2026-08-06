package com.shreyash.dotrack.core.util

import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.model.Task
import java.time.LocalDateTime

object TaskSorter {

    fun sort(tasks: List<Task>, option: SortOption, direction: SortDirection): List<Task> {
        val sorted = when (option) {
            SortOption.DUE_DATE -> tasks.sortedBy { it.dueDate ?: LocalDateTime.MAX }
            SortOption.PRIORITY -> tasks.sortedByDescending { it.priority.value }
            SortOption.CREATED_DATE -> tasks.sortedBy { it.createdAt }
            SortOption.TITLE -> tasks.sortedBy { it.title.lowercase() }
        }
        return if (direction == SortDirection.DESCENDING) sorted.reversed() else sorted
    }
}