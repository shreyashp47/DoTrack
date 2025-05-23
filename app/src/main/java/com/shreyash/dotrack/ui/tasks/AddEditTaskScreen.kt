package com.shreyash.dotrack.ui.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.domain.model.Priority
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
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
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = result.exceptionOrNull()?.message ?: "Error saving task"
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                    contentDescription = "Save Task"
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
    var selectedTime by remember { mutableStateOf<LocalTime?>(dueDate?.toLocalTime() ?: LocalTime.of(12, 0)) }

    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Due Date Picker
        OutlinedTextField(
            value = dueDate?.format(dateFormatter) ?: "No due date",
            onValueChange = {},
            label = { Text("Due Date (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date"
                    )
                }
            }
        )

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = dueDate?.atZone(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli()
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = LocalDate.ofInstant(
                                    Instant.ofEpochMilli(millis),
                                    ZoneId.systemDefault()
                                )
                                showDatePicker = false
                                showTimePicker = true
                            }
                        }
                    ) {
                        Text("Next")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        
        // Time Picker Dialog
        if (showTimePicker) {
            TimePickerDialog(
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
                label = { Text("Priority") },
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
                        Text("Enable reminder (30 min before)")
                    }
                    Text(
                        text = "You'll receive a notification 30 minutes before the due time",
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
                Text("Clear Due Date")
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
                    title = { Text("Add Task") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save Task"
                    )
                }
            }
        ) { padding ->
            TaskForm(
                title = "Sample Task",
                onTitleChange = {},
                description = "This is a sample task description for preview",
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

@Preview(showBackground = true)
@Composable
fun TaskFormPreview() {
    MaterialTheme {
        TaskForm(
            title = "Sample Task",
            onTitleChange = {},
            description = "This is a sample task description for preview",
            onDescriptionChange = {},
            priority = Priority.MEDIUM,
            onPriorityChange = {},
            dueDate = LocalDateTime.now().plusDays(3),
            //onDueDateClick = {},
            onDueDateChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    var hour by remember { mutableStateOf(12) }
    var minute by remember { mutableStateOf(0) }
    
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Hour picker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hour:", modifier = Modifier.width(60.dp))
                    Slider(
                        value = hour.toFloat(),
                        onValueChange = { hour = it.toInt() },
                        valueRange = 0f..23f,
                        steps = 23,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = hour.toString().padStart(2, '0'),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Minute picker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Minute:", modifier = Modifier.width(60.dp))
                    Slider(
                        value = minute.toFloat(),
                        onValueChange = { minute = it.toInt() },
                        valueRange = 0f..59f,
                        steps = 59,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = minute.toString().padStart(2, '0'),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Display selected time
                Text(
                    text = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = { onTimeSelected(hour, minute) }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}