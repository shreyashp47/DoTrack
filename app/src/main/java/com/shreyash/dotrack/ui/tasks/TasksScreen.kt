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
import androidx.compose.material3.LinearProgressIndicator
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
            // Wallpaper was updated
        }
    }

    val allTasksDeleted = stringResource(R.string.all_tasks_deleted)

    // Delete confirmation dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text(stringResource(R.string.delete_all_tasks)) },
            text = { Text(stringResource(R.string.delete_all_confirm)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllTask()
                        showDeleteConfirmDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar(allTasksDeleted)
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_all_tasks))
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
    val wallpaperInProgress = stringResource(R.string.wallpaper_in_progress)

    // Delete confirmation dialog
    if (showSetWallpaperDialog) {
        AlertDialog(
            onDismissRequest = { showSetWallpaperDialog = false },
            title = { Text(stringResource(R.string.set_wallpaper)) },
            text = { Text(stringResource(R.string.set_wallpaper_confirm)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateWallpaper()
                        showSetWallpaperDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar(wallpaperInProgress)
                        }
                    }
                ) {
                    Text(stringResource(R.string.apply))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSetWallpaperDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            contentDescription = stringResource(R.string.set_as_wallpaper)
                        )
                    }
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_all_tasks_content_desc)
                        )
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
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
                            text = stringResource(R.string.no_tasks_yet),
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
                        onDeleteTask = { taskId -> viewModel.deleteTask(taskId) },
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
                        text = stringResource(R.string.error_format, tasksState.exceptionOrNull()?.message ?: ""),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
    }

    if (viewModel.isUpdatingWallpaper) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.applying_wallpaper),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit,
    onTaskCheckChange: (Task, Boolean) -> Unit,
    onDeleteTask: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
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
        onClick = {
            showDeleteConfirmDialog = true
        }
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