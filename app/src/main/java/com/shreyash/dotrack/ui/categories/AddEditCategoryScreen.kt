package com.shreyash.dotrack.ui.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    categoryId: String?,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (categoryId == null) "Add Category" else "Edit Category") },
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
                onClick = {
                    // viewModel.saveTask()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Task"
                )
            }
        },
        snackbarHost = { }
    ) { padding ->
        Box() {
            Text(text = "Hello")
        }
//
//            TaskForm(
//                title = uiState.title,
//                onTitleChange = viewModel::updateTitle,
//                description = uiState.description,
//                onDescriptionChange = viewModel::updateDescription,
//                dueDate = uiState.dueDate,
//                onDueDateChange = viewModel::updateDueDate,
//                priority = uiState.priority,
//                onPriorityChange = viewModel::updatePriority,
//                modifier = Modifier.padding(padding)
//            )
//
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditCategoryScreenPreview() {
    MaterialTheme {
        AddEditCategoryScreen(
            categoryId = null,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditCategoryScreenPreview() {
    MaterialTheme {
        AddEditCategoryScreen(
            categoryId = "1",
            onBackClick = {}
        )
    }
}