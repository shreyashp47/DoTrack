package com.shreyash.dotrack.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreyash.dotrack.domain.model.Category

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    MaterialTheme {
        CategoriesScreen(
            onCategoryClick = {},
            onAddCategoryClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onCategoryClick: (String) -> Unit,
    onAddCategoryClick: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoriesState by viewModel.categories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCategoryClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Category"
                )
            }
        }
    ) { padding ->
        when {
            categoriesState.isLoading() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            categoriesState.isSuccess() -> {
                val categories = categoriesState.getOrNull() ?: emptyList()
                if (categories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No categories yet. Add one!")
                    }
                } else {
                    CategoryList(
                        categories = categories,
                        onCategoryClick = onCategoryClick,
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            categoriesState.isError() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${categoriesState.exceptionOrNull()?.message}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListPreview() {
    MaterialTheme {
        CategoryList(
            categories = listOf(
                Category(
                    id = "1",
                    name = "Work",
                    color = 0xFFE57373.toInt()
                ),
                Category(
                    id = "2",
                    name = "Personal",
                    color = 0xFF81C784.toInt()
                ),
                Category(
                    id = "3",
                    name = "Shopping",
                    color = 0xFF64B5F6.toInt()
                )
            ),
            onCategoryClick = {}
        )
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    MaterialTheme {
        CategoryItem(
            category = Category(
                id = "1",
                name = "Work",
                color = 0xFFE57373.toInt()
            ),
            onClick = {}
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(category.color))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}