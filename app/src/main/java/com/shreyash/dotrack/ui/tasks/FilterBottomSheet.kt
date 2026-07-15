package com.shreyash.dotrack.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    currentPriorityFilter: Priority?,
    currentStatusFilter: Boolean?,
    onPriorityFilterSelected: (Priority?) -> Unit,
    onStatusFilterSelected: (Boolean?) -> Unit,
    onReset: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.filter_by),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            Text(
                text = stringResource(R.string.filter_status),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 4.dp)
            )

            SelectableOptionItem(
                label = stringResource(R.string.filter_all),
                isSelected = currentStatusFilter == null,
                onClick = { onStatusFilterSelected(null); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.filter_active),
                isSelected = currentStatusFilter == false,
                onClick = { onStatusFilterSelected(false); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.filter_completed),
                isSelected = currentStatusFilter == true,
                onClick = { onStatusFilterSelected(true); onDismiss() }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            Text(
                text = stringResource(R.string.priority),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 4.dp)
            )

            SelectableOptionItem(
                label = stringResource(R.string.filter_all),
                isSelected = currentPriorityFilter == null,
                onClick = { onPriorityFilterSelected(null); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.high_priority),
                isSelected = currentPriorityFilter == Priority.HIGH,
                onClick = { onPriorityFilterSelected(Priority.HIGH); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.medium_priority),
                isSelected = currentPriorityFilter == Priority.MEDIUM,
                onClick = { onPriorityFilterSelected(Priority.MEDIUM); onDismiss() }
            )
            SelectableOptionItem(
                label = stringResource(R.string.low_priority),
                isSelected = currentPriorityFilter == Priority.LOW,
                onClick = { onPriorityFilterSelected(Priority.LOW); onDismiss() }
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
