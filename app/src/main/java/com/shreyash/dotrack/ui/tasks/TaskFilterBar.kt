package com.shreyash.dotrack.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption

@Composable
fun TaskFilterBar(
    sortOption: SortOption,
    sortDirection: SortDirection,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onSortClick) {
            Text(
                text = stringResource(
                    when (sortOption) {
                        SortOption.DUE_DATE -> R.string.sort_due_date
                        SortOption.PRIORITY -> R.string.sort_priority
                        SortOption.CREATED_DATE -> R.string.sort_created_date
                        SortOption.TITLE -> R.string.sort_title
                    }
                ),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = if (sortDirection == SortDirection.ASCENDING)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        TextButton(onClick = onFilterClick) {
            Text(
                text = stringResource(R.string.filter_by),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskFilterBarPreview() {
    MaterialTheme {
        TaskFilterBar(
            sortOption = SortOption.DUE_DATE,
            sortDirection = SortDirection.ASCENDING,
            onSortClick = {},
            onFilterClick = {}
        )
    }
}
