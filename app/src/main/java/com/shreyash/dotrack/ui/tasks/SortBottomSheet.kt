package com.shreyash.dotrack.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun SortBottomSheet(
    expanded: Boolean,
    onDismiss: () -> Unit,
    currentSort: SortOption,
    currentDirection: SortDirection,
    onSortOptionSelected: (SortOption) -> Unit,
    onToggleDirection: () -> Unit
) {
    if (expanded) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = onDismiss,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                SortOptionEntry(
                    label = stringResource(R.string.sort_due_date),
                    selected = currentSort == SortOption.DUE_DATE,
                    onClick = { onSortOptionSelected(SortOption.DUE_DATE); onDismiss() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SortOptionEntry(
                    label = stringResource(R.string.sort_priority),
                    selected = currentSort == SortOption.PRIORITY,
                    onClick = { onSortOptionSelected(SortOption.PRIORITY); onDismiss() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SortOptionEntry(
                    label = stringResource(R.string.sort_created_date),
                    selected = currentSort == SortOption.CREATED_DATE,
                    onClick = { onSortOptionSelected(SortOption.CREATED_DATE); onDismiss() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SortOptionEntry(
                    label = stringResource(R.string.sort_title),
                    selected = currentSort == SortOption.TITLE,
                    onClick = { onSortOptionSelected(SortOption.TITLE); onDismiss() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                DirectionSelection(
                    currentDirection = currentDirection,
                    onToggleDirection = onToggleDirection
                )
            }
        }
    }
}

@Composable
private fun SortOptionEntry(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun DirectionSelection(
    currentDirection: SortDirection,
    onToggleDirection: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleDirection() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = if (currentDirection == SortDirection.ASCENDING)
                stringResource(R.string.sort_ascending) else stringResource(R.string.sort_descending),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (currentDirection == SortDirection.ASCENDING)
                Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun SortBottomSheetPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            SortBottomSheet(
                expanded = true,
                onDismiss = {},
                currentSort = SortOption.DUE_DATE,
                currentDirection = SortDirection.ASCENDING,
                onSortOptionSelected = {},
                onToggleDirection = {}
            )
        }
    }
}