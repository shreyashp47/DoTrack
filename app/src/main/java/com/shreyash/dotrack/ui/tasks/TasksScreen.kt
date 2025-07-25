package com.shreyash.dotrack.ui.tasks

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.R
import com.shreyash.dotrack.core.ui.theme.CardColorHighPriority
import com.shreyash.dotrack.core.ui.theme.CardColorLowPriority
import com.shreyash.dotrack.core.ui.theme.CardColorMediumPriority
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()

) {
    val tasksState by viewModel.tasks.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showSetWallpaperDialog by remember { mutableStateOf(false) }

    // Monitor wallpaper updates
    LaunchedEffect(viewModel.wallpaperUpdated) {
        if (viewModel.wallpaperUpdated) {
            scope.launch {
                //snackbarHostState.showSnackbar("Wallpaper updated with your tasks")
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete All Tasks") },
            text = { Text("Are you sure you want to delete all tasks? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllTask()
                        showDeleteConfirmDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("All tasks deleted")
                        }
                    }
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    // Delete confirmation dialog
    if (showSetWallpaperDialog) {
        AlertDialog(
            onDismissRequest = { showSetWallpaperDialog = false },
            title = { Text("Set Wallpaper") },
            text = { Text("Are you sure you want set wallpaper? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateWallpaper()
                        showSetWallpaperDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("New wallpaper is progress... . ")
                        }
                    }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSetWallpaperDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.app_name)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showSetWallpaperDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_wallpaper),
                            contentDescription = "Set as Wallpaper"
                        )
                    }
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete all tasks"
                        )
                    }
                },
                modifier = Modifier.statusBarsPadding(),
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                        Text(
                            text = "No tasks yet. Add one!",
                            color = MaterialTheme.colorScheme.onBackground
                        )
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
                    Text(
                        text = "Error: ${tasksState.exceptionOrNull()?.message}",
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
    modifier: Modifier = Modifier,
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
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
                        text = "Due: ${it.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor
                    )
                }
            }

            PriorityIndicator(
                priority = task.priority,
                taskId = task.id
            )
        }
    }
}

@Composable
fun PriorityIndicator(
    priority: Priority,
    taskId: String,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val color = when (priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        Priority.LOW -> MaterialTheme.colorScheme.primary
    }

    // State for delete confirmation dialog
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Delete confirmation dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTask(taskId)
                        showDeleteConfirmDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Task deleted")
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    IconButton(
        onClick = {
            showDeleteConfirmDialog = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete task",
            tint = color
        )
    }
}


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

@RequiresApi(Build.VERSION_CODES.O)
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