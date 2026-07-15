package com.shreyash.dotrack.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.model.SortDirection
import com.shreyash.dotrack.domain.model.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropdownMenu(
    onDismiss: () -> Unit,
    currentSort: SortOption,
    currentDirection: SortDirection,
    onSortOptionSelected: (SortOption) -> Unit,
    onToggleDirection: () -> Unit,
    onReset: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            SelectableOptionItem(
                label = stringResource(R.string.sort_due_date),
                isSelected = currentSort == SortOption.DUE_DATE,
                onClick = { onSortOptionSelected(SortOption.DUE_DATE); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.sort_priority),
                isSelected = currentSort == SortOption.PRIORITY,
                onClick = { onSortOptionSelected(SortOption.PRIORITY); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.sort_created_date),
                isSelected = currentSort == SortOption.CREATED_DATE,
                onClick = { onSortOptionSelected(SortOption.CREATED_DATE); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.sort_title),
                isSelected = currentSort == SortOption.TITLE,
                onClick = { onSortOptionSelected(SortOption.TITLE); onDismiss() }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            SortDirectionItem(
                isAscending = currentDirection == SortDirection.ASCENDING,
                onClick = { onToggleDirection(); onDismiss() }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            TextButton(
                onClick = { onReset(); onDismiss() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SortDirectionItem(
    isAscending: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isAscending) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(
                text = if (isAscending) stringResource(R.string.sort_ascending)
                else stringResource(R.string.sort_descending),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
