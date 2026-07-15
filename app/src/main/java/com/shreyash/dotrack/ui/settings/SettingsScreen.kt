package com.shreyash.dotrack.ui.settings

import android.Manifest
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.shreyash.dotrack.R
import com.shreyash.dotrack.domain.model.Priority

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsPreviewContent() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingSwitchItem(
                        title = stringResource(R.string.auto_wallpaper_updates),
                        subtitle = stringResource(R.string.auto_wallpaper_subtitle),
                        checked = false,
                        onCheckedChange = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(stringResource(R.string.wallpaper_color), style = MaterialTheme.typography.bodyLarge)
                    Text(stringResource(R.string.wallpaper_color_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WallpaperColorCircle(colorHex = "#1A2980", label = "Primary", onClick = {})
                        WallpaperColorCircle(colorHex = "#26D0CE", label = "Secondary", onClick = {})
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.task_priority_colors), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ColorSettingItem(title = stringResource(R.string.high_priority), subtitle = stringResource(R.string.high_priority_subtitle), colorHex = "#FF4444", onClick = {})
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ColorSettingItem(title = stringResource(R.string.medium_priority), subtitle = stringResource(R.string.medium_priority_subtitle), colorHex = "#FFBB33", onClick = {})
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ColorSettingItem(title = stringResource(R.string.low_priority), subtitle = stringResource(R.string.low_priority_subtitle), colorHex = "#00C851", onClick = {})
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.notifications), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingSwitchItem(title = stringResource(R.string.permission), subtitle = stringResource(R.string.enable_notification_permission), checked = false, onCheckedChange = {})
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {}, shape = RoundedCornerShape(12.dp)) {
                Icon(painterResource(id = R.drawable.ic_sync), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.sync_up))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    if (LocalInspectionMode.current) {
        SettingsPreviewContent()
        return
    }

    // Collect states from ViewModel
    val isAutoWallpaperEnabled by viewModel.autoWallpaperEnabled.collectAsState()
    val currentWallpaperColor by viewModel.wallpaperColor.collectAsState()
    val secondaryWallpaperColor by viewModel.wallpaperSecondaryColor.collectAsState()
    val currentDarkMode by viewModel.darkMode.collectAsState()

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
                ColorPickerMode.WALLPAPER -> stringResource(R.string.choose_wallpaper_color)
                ColorPickerMode.SECONDARY_WALLPAPER -> stringResource(R.string.choose_secondary_wallpaper_color)
                ColorPickerMode.HIGH_PRIORITY -> stringResource(R.string.choose_high_priority_color)
                ColorPickerMode.MEDIUM_PRIORITY -> stringResource(R.string.choose_medium_priority_color)
                ColorPickerMode.LOW_PRIORITY -> stringResource(R.string.choose_low_priority_color)
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
    LaunchedEffect(secondaryWallpaperColor) {
        Log.d("WallpaperColor", "Updated color: $secondaryWallpaperColor")
    }

    // Update notification permission state when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateNotificationPermissionState()
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Wallpaper Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingSwitchItem(
                        title = stringResource(R.string.auto_wallpaper_updates),
                        subtitle = stringResource(R.string.auto_wallpaper_subtitle),
                        checked = isAutoWallpaperEnabled,
                        onCheckedChange = { viewModel.setAutoWallpaperEnabled(it) }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    Text(
                        text = stringResource(R.string.wallpaper_color),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.wallpaper_color_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        WallpaperColorCircle(
                            colorHex = currentWallpaperColor,
                            label = "Primary",
                            onClick = { viewModel.showWallpaperColorPicker() }
                        )
                        WallpaperColorCircle(
                            colorHex = secondaryWallpaperColor,
                            label = "Secondary",
                            onClick = { viewModel.showSecondaryWallpaperColorPicker() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Priority Colors Section
            Text(
                text = stringResource(R.string.task_priority_colors),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val currentHighPriorityColor by viewModel.highPriorityColor.collectAsState()
                    ColorSettingItem(
                        title = stringResource(R.string.high_priority),
                        subtitle = stringResource(R.string.high_priority_subtitle),
                        colorHex = currentHighPriorityColor,
                        onClick = { viewModel.showPriorityColorPicker(Priority.HIGH) }
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    val currentMediumPriorityColor by viewModel.mediumPriorityColor.collectAsState()
                    ColorSettingItem(
                        title = stringResource(R.string.medium_priority),
                        subtitle = stringResource(R.string.medium_priority_subtitle),
                        colorHex = currentMediumPriorityColor,
                        onClick = { viewModel.showPriorityColorPicker(Priority.MEDIUM) }
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    val currentLowPriorityColor by viewModel.lowPriorityColor.collectAsState()
                    ColorSettingItem(
                        title = stringResource(R.string.low_priority),
                        subtitle = stringResource(R.string.low_priority_subtitle),
                        colorHex = currentLowPriorityColor,
                        onClick = { viewModel.showPriorityColorPicker(Priority.LOW) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Appearance Section
            Text(
                text = stringResource(R.string.appearance),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DarkModeOption(
                        label = stringResource(R.string.dark_mode_system),
                        selected = currentDarkMode == "system",
                        onClick = { viewModel.setDarkMode("system") }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    DarkModeOption(
                        label = stringResource(R.string.dark_mode_light),
                        selected = currentDarkMode == "light",
                        onClick = { viewModel.setDarkMode("light") }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    DarkModeOption(
                        label = stringResource(R.string.dark_mode_dark),
                        selected = currentDarkMode == "dark",
                        onClick = { viewModel.setDarkMode("dark") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notifications Section
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingSwitchItem(
                        title = stringResource(R.string.permission),
                        subtitle = stringResource(R.string.enable_notification_permission),
                        checked = isNotificationEnabled,
                        onCheckedChange = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (!isNotificationEnabled) {
                                    showPermissionDialog = true
                                }
                            }
                        }
                    )
                }
            }

            // Permission request dialog
            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text(stringResource(R.string.notification_permission_title)) },
                    text = { Text(stringResource(R.string.notification_permission_message)) },
                    confirmButton = {
                        Button(
                            onClick = {
                                showPermissionDialog = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        ) {
                            Text(stringResource(R.string.grant_permission))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showPermissionDialog = false }
                        ) {
                            Text(stringResource(R.string.not_now))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.updateWallpaper() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_sync),
                    contentDescription = stringResource(R.string.sync_up_content_desc),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.sync_up))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    title: String,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
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
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onColorSelected(controller.selectedColor.value)

                        }
                    ) {
                        Text(stringResource(R.string.apply))
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
fun WallpaperColorCircle(
    colorHex: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(colorHex)))
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DarkModeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
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

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(colorHex)))
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.choose_color),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorPickerDialogPreview() {
    MaterialTheme {
        ColorPickerDialog(
            initialColor = Color(0xFF1A2980),
            title = "Choose Color",
            onColorSelected = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingSwitchItemPreview() {
    MaterialTheme {
        SettingSwitchItem(
            title = "Auto Wallpaper Updates",
            subtitle = "Automatically update wallpaper when tasks change",
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WallpaperColorCirclePreview() {
    MaterialTheme {
        WallpaperColorCircle(
            colorHex = "#1A2980",
            label = "Primary",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DarkModeOptionPreview() {
    MaterialTheme {
        DarkModeOption(
            label = "System Default",
            selected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorSettingItemPreview() {
    MaterialTheme {
        ColorSettingItem(
            title = "High Priority",
            subtitle = "Color for high priority tasks",
            colorHex = "#FF4444",
            onClick = {}
        )
    }
}

