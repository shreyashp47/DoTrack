package com.shreyash.dotrack.ui.settings

import com.shreyash.dotrack.R
import android.util.Log
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.shreyash.dotrack.domain.model.Priority
import com.shreyash.dotrack.ui.tasks.TasksViewModel

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // Collect states from ViewModel
    val isAutoWallpaperEnabled by viewModel.autoWallpaperEnabled.collectAsState()
    val currentWallpaperColor by viewModel.wallpaperColor.collectAsState()
    
    // Notification permission state
    val isNotificationEnabled = viewModel.notificationPermissionState
    
    // Permission request dialog state
    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateNotificationPermissionState()
    }

    // Show color picker dialog if needed
    if (viewModel.showColorPickerDialog) {
        ColorPickerDialog(
            initialColor = viewModel.selectedColor,
            title = when (viewModel.currentColorPickerMode) {
                ColorPickerMode.WALLPAPER -> "Choose Wallpaper Color"
                ColorPickerMode.HIGH_PRIORITY -> "Choose High Priority Color"
                ColorPickerMode.MEDIUM_PRIORITY -> "Choose Medium Priority Color"
                ColorPickerMode.LOW_PRIORITY -> "Choose Low Priority Color"
            },
            onColorSelected = { color ->
                viewModel.updateSelectedColor(color)
                viewModel.applySelectedColor()
            },
            onDismiss = { viewModel.hideColorPicker() }
        )
    }
    LaunchedEffect(currentWallpaperColor) {
        Log.d("WallpaperColor", "Updated color: $currentWallpaperColor")
    }
    
    // Update notification permission state when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateNotificationPermissionState()
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            SettingSwitchItem(
                title = "Auto Wallpaper Updates",
                subtitle = "Automatically update wallpaper when tasks change",
                checked = isAutoWallpaperEnabled,
                onCheckedChange = { viewModel.setAutoWallpaperEnabled(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Current wallpaper color preview
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showWallpaperColorPicker() }
                    .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Wallpaper Color",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Choose background color for wallpaper",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Color preview
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(currentWallpaperColor)))
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Choose color",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority Colors Section
            Text(
                text = "Task Priority Colors",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // High Priority Color
            val currentHighPriorityColor by viewModel.highPriorityColor.collectAsState()
            ColorSettingItem(
                title = "High Priority",
                subtitle = "Color for high priority tasks",
                colorHex = currentHighPriorityColor,
                onClick = { viewModel.showPriorityColorPicker(Priority.HIGH) }
            )

            // Medium Priority Color
            val currentMediumPriorityColor by viewModel.mediumPriorityColor.collectAsState()
            ColorSettingItem(
                title = "Medium Priority",
                subtitle = "Color for medium priority tasks",
                colorHex = currentMediumPriorityColor,
                onClick = { viewModel.showPriorityColorPicker(Priority.MEDIUM) }
            )

            // Low Priority Color
            val currentLowPriorityColor by viewModel.lowPriorityColor.collectAsState()
            ColorSettingItem(
                title = "Low Priority",
                subtitle = "Color for low priority tasks",
                colorHex = currentLowPriorityColor,
                onClick = { viewModel.showPriorityColorPicker(Priority.LOW) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications Section
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingSwitchItem(
                title = "Permission",
                subtitle = "Enable notifications permissions",
                checked = isNotificationEnabled,
                onCheckedChange = { 
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (!isNotificationEnabled) {
                            // Show permission dialog
                            showPermissionDialog = true
                        }
                    }
                }
            )
            
            // Permission request dialog
            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text("Notification Permission") },
                    text = { Text("DoTrack needs notification permission to send you reminders for your tasks. Would you like to grant this permission?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showPermissionDialog = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        ) {
                            Text("Grant Permission")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showPermissionDialog = false }
                        ) {
                            Text("Not Now")
                        }
                    }
                )
            }

            val tasksViewModel: TasksViewModel = hiltViewModel()
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    tasksViewModel.updateWallpaper()
                }) {
                Text(
                    text = "Sync-up",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Icon(
                    painterResource(id = R.drawable.ic_sync),
                    contentDescription = "Sync up"
                )
            }
        }
    }
}

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    title: String,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = onDismiss) {
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
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color picker
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    controller = controller,
                    initialColor = initialColor,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        // Update the selected color in real-time if needed
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Brightness slider
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color preview
                AlphaTile(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onColorSelected(controller.selectedColor.value)
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingSwitchItem(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ColorSettingItem(
    title: String,
    subtitle: String? = null,
    colorHex: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Color preview
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(colorHex)))
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Choose color",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
