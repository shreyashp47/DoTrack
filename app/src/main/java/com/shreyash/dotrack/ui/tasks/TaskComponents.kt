package com.shreyash.dotrack.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shreyash.dotrack.R
import com.shreyash.dotrack.core.ui.theme.CardColorHighPriority
import com.shreyash.dotrack.core.ui.theme.CardColorLowPriority
import com.shreyash.dotrack.core.ui.theme.CardColorMediumPriority
import com.shreyash.dotrack.core.ui.theme.DoTrackTheme
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit,
    onTaskCheckChange: (Task, Boolean) -> Unit,
    onDeleteTask: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onClick = { onTaskClick(task.id) },
                onCheckChange = { isCompleted -> onTaskCheckChange(task, isCompleted) },
                onDeleteTask = onDeleteTask
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onCheckChange: (Boolean) -> Unit,
    onDeleteTask: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
    val color = when (task.priority) {
        Priority.HIGH -> CardColorHighPriority
        Priority.MEDIUM -> CardColorMediumPriority
        Priority.LOW -> CardColorLowPriority
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckChange
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                val isDarkTheme = isSystemInDarkTheme()
                val textColor = if (isDarkTheme) Color.White else Color.Black

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = textColor
                    )
                }

                task.dueDate?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.due_label, it.format(dateFormatter)),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor
                    )
                }
            }

            PriorityIndicator(
                priority = task.priority,
                taskId = task.id,
                onDeleteTask = onDeleteTask
            )
        }
    }
}

@Composable
fun PriorityIndicator(
    priority: Priority,
    taskId: String,
    onDeleteTask: (String) -> Unit = {}
) {
    val color = when (priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        Priority.LOW -> MaterialTheme.colorScheme.primary
    }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text(stringResource(R.string.delete_task)) },
            text = { Text(stringResource(R.string.delete_task_confirm)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTask(taskId)
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    IconButton(
        onClick = { showDeleteConfirmDialog = true }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_task_content_desc),
            tint = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    DoTrackTheme {
        TaskItem(
            task = Task(
                id = "1",
                title = "Complete project",
                description = "Finish the Android project by end of week",
                isCompleted = false,
                dueDate = LocalDateTime.now().plusDays(2),
                priority = Priority.HIGH,
                categoryId = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            onClick = {},
            onCheckChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityIndicatorPreview() {
    DoTrackTheme {
        PriorityIndicator(
            priority = Priority.HIGH,
            taskId = "1"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    DoTrackTheme {
        TaskList(
            tasks = listOf(
                Task(
                    id = "1",
                    title = "Complete project",
                    description = "Finish the Android project by end of week",
                    isCompleted = false,
                    dueDate = LocalDateTime.now().plusDays(2),
                    priority = Priority.HIGH,
                    categoryId = null,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                ),
                Task(
                    id = "2",
                    title = "Buy groceries",
                    description = "Milk, eggs, bread",
                    isCompleted = true,
                    dueDate = LocalDateTime.now(),
                    priority = Priority.MEDIUM,
                    categoryId = null,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                ),
                Task(
                    id = "3",
                    title = "Read book",
                    description = "Chapter 5-7",
                    isCompleted = true,
                    dueDate = LocalDateTime.now(),
                    priority = Priority.LOW,
                    categoryId = null,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            ),
            onTaskClick = {},
            onTaskCheckChange = { _, _ -> }
        )
    }
}
