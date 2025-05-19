package com.shreyash.dotrack.ui.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.core.util.Result
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import java.time.format.DateTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasksState by viewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { padding ->
        when {
            tasksState.isLoading() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            tasksState.isSuccess() -> {
                val tasks = tasksState.getOrNull() ?: emptyList()
                if (tasks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tasks yet. Add one!")
                    }
                } else {
                    TaskList(
                        tasks = tasks,
                        onTaskClick = onTaskClick,
                        onTaskCheckChange = { task, isCompleted ->
                            if (isCompleted) {
                                viewModel.completeTask(task.id)
                            } else {
                                viewModel.uncompleteTask(task.id)
                            }
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
            tasksState.isError() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${tasksState.exceptionOrNull()?.message}")
                }
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit,
    onTaskCheckChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onClick = { onTaskClick(task.id) },
                onCheckChange = { isCompleted -> onTaskCheckChange(task, isCompleted) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onCheckChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckChange
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                task.dueDate?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Due: ${it.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            PriorityIndicator(priority = task.priority)
        }
    }
}

@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        Priority.LOW -> MaterialTheme.colorScheme.primary
    }

    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "Priority ${priority.name}",
        tint = color
    )
}

@Preview(showBackground = true)
@Composable
fun PriorityIndicatorPreview() {
    MaterialTheme {
        PriorityIndicator(priority = Priority.HIGH)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    MaterialTheme {
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    MaterialTheme {
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
                    categoryId = null,createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            ),
            onTaskClick = {},
            onTaskCheckChange = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    MaterialTheme {
        TasksScreen(
            onTaskClick = {},
            onAddTaskClick = {}
        )
    }
}