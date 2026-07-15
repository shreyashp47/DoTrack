package com.shreyash.dotrack.ui.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.R
import com.shreyash.dotrack.core.ui.theme.DoTrackTheme
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import com.shreyash.dotrack.domain.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasksState by viewModel.sortedTasks.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showSetWallpaperDialog by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.wallpaperUpdated) {
        if (viewModel.wallpaperUpdated) { }
    }

    val allTasksDeleted = stringResource(R.string.all_tasks_deleted)

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
                        scope.launch { snackbarHostState.showSnackbar(allTasksDeleted) }
                    }
                ) { Text(stringResource(R.string.delete_all_tasks)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    val wallpaperInProgress = stringResource(R.string.wallpaper_in_progress)

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
                        scope.launch { snackbarHostState.showSnackbar(wallpaperInProgress) }
                    }
                ) { Text(stringResource(R.string.apply)) }
            },
            dismissButton = {
                TextButton(onClick = { showSetWallpaperDialog = false }) {
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
                        IconButton(onClick = { showSetWallpaperDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_wallpaper),
                                contentDescription = stringResource(R.string.set_as_wallpaper)
                            )
                        }
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
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
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TaskFilterBar(
                        sortOption = viewModel.sortOption,
                        sortDirection = viewModel.sortDirection,
                        onSortClick = { showSortMenu = true },
                        onFilterClick = { showFilterSheet = true }
                    )

                    Box(modifier = Modifier.weight(1f)) {
                    when {
                        tasksState.isLoading() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }
                        }

                        tasksState.isSuccess() -> {
                            val tasks = tasksState.getOrNull() ?: emptyList()
                            if (tasks.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
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
                                        if (isCompleted) viewModel.completeTask(task.id)
                                        else viewModel.uncompleteTask(task.id)
                                    },
                                    onDeleteTask = { taskId -> viewModel.deleteTask(taskId) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        tasksState.isError() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
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

            if (showSortMenu) {
                SortDropdownMenu(
                    onDismiss = { showSortMenu = false },
                    currentSort = viewModel.sortOption,
                    currentDirection = viewModel.sortDirection,
                    onSortOptionSelected = { viewModel.setSortOption(it) },
                    onToggleDirection = { viewModel.toggleSortDirection() },
                    onReset = { viewModel.resetSort() }
                )
            }

            if (showFilterSheet) {
                FilterBottomSheet(
                    onDismiss = { showFilterSheet = false },
                    currentPriorityFilter = viewModel.filterPriority,
                    currentStatusFilter = viewModel.filterCompleted,
                    onPriorityFilterSelected = { viewModel.setFilterPriority(it) },
                    onStatusFilterSelected = { viewModel.setFilterStatus(it) },
                    onReset = { viewModel.resetFilter() }
                )
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
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.applying_wallpaper),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TasksScreenPreview() {
    DoTrackTheme {
        TasksScreenPreviewContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TasksScreenPreviewContent() {
    val tasks = listOf(
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
    )

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
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_wallpaper),
                            contentDescription = stringResource(R.string.set_as_wallpaper)
                        )
                    }
                    IconButton(onClick = {}) {
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
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TaskFilterBar(
                sortOption = SortOption.DUE_DATE,
                sortDirection = SortDirection.ASCENDING,
                onSortClick = {},
                onFilterClick = {}
            )

            Box(modifier = Modifier.weight(1f)) {
                TaskList(
                    tasks = tasks,
                    onTaskClick = {},
                    onTaskCheckChange = { _, _ -> },
                    onDeleteTask = {}
                )
            }
        }
    }
}
