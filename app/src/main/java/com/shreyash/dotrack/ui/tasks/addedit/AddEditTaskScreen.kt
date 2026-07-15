package com.shreyash.dotrack.ui.tasks.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shreyash.dotrack.R
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.domain.model.Priority
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    onBackClick: () -> Unit,
    onTaskSaved: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorSavingTaskMessage = stringResource(R.string.error_saving_task)

    // Load task if editing
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    // Handle save result
    LaunchedEffect(uiState.saveResult) {
        uiState.saveResult?.let { result ->
            if (result.isSuccess()) {
                onTaskSaved()
            } else if (result.isError()) {
                val errorMessage = result.exceptionOrNull()?.message ?: errorSavingTaskMessage
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = errorMessage
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) stringResource(R.string.add_task) else stringResource(R.string.edit_task)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveTask() }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_task)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            TaskForm(
                title = uiState.title,
                onTitleChange = viewModel::updateTitle,
                description = uiState.description,
                onDescriptionChange = viewModel::updateDescription,
                dueDate = uiState.dueDate,
                onDueDateChange = viewModel::updateDueDate,
                priority = uiState.priority,
                onPriorityChange = viewModel::updatePriority,
                reminderEnabled = uiState.reminderEnabled,
                onReminderEnabledChange = viewModel::updateReminderEnabled,
                modifier = Modifier.padding(padding)
            )
        }
    }
    }

    if (uiState.isSaving) {
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
                    text = stringResource(R.string.saving_task),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    dueDate: LocalDateTime?,
    onDueDateChange: (LocalDateTime?) -> Unit,
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
    reminderEnabled: Boolean = false,
    onReminderEnabledChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(dueDate?.toLocalDate()) }
    var selectedTime by remember {
        mutableStateOf<LocalTime?>(
            dueDate?.toLocalTime() ?: LocalTime.of(12, 0)
        )
    }

    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.title_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.description_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Due Date Picker
        OutlinedTextField(
            value = dueDate?.format(dateFormatter) ?: stringResource(R.string.no_due_date),
            onValueChange = {},
            label = { Text(stringResource(R.string.due_date_optional)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_date)
                    )
                }
            }
        )

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = (selectedDate ?: LocalDate.now())
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= LocalDate.now()
                            .atStartOfDay(ZoneOffset.UTC)
                            .toInstant()
                            .toEpochMilli()
                    }
                }
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                showDatePicker = false
                                showTimePicker = true
                            }
                        }
                    ) {
                        Text(stringResource(R.string.next))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Time Picker Dialog
        if (showTimePicker) {
            TimePickerDialog(
                selectedDate = selectedDate ?: LocalDate.now(),
                onDismissRequest = {
                    showTimePicker = false
                    // If user cancels time selection, reset date selection too
                    if (dueDate == null) selectedDate = null
                },
                onTimeSelected = { hour, minute ->
                    selectedTime = LocalTime.of(hour, minute)
                    selectedDate?.let { date ->
                        onDueDateChange(LocalDateTime.of(date, selectedTime))
                    }
                    showTimePicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Priority Dropdown
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = priority.name,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.priority)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                Priority.values().forEach { priorityOption ->
                    DropdownMenuItem(
                        text = { Text(priorityOption.name) },
                        onClick = {
                            onPriorityChange(priorityOption)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Reminder switch
            if (dueDate != null) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = onReminderEnabledChange
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.enable_reminder))
                    }
                    Text(
                        text = stringResource(R.string.reminder_helper),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Clear due date button
            Button(
                onClick = { onDueDateChange(null) },
                enabled = dueDate != null,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.clear_due_date))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddEditTaskScreenPreview() {
    MaterialTheme {
        // Create a simplified version of the screen for preview
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.add_task)) },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.save_task)
                    )
                }
            }
        ) { padding ->
            TaskForm(
                title = stringResource(R.string.sample_task),
                onTitleChange = {},
                description = stringResource(R.string.sample_task_description),
                onDescriptionChange = {},
                priority = Priority.MEDIUM,
                onPriorityChange = {},
                dueDate = LocalDateTime.now().plusDays(3),
                //onDueDateClick = {},
                onDueDateChange = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

