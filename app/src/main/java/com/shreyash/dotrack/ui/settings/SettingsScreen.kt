package com.shreyash.dotrack.ui.settings

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
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
        SettingsContent(
            modifier = Modifier.padding(padding),
            isAutoWallpaperEnabled = false,
            currentWallpaperColor = "#1A2980",
            secondaryWallpaperColor = "#26D0CE",
            currentDarkMode = "system",
            isNotificationEnabled = false,
            highPriorityColor = "#FF4444",
            mediumPriorityColor = "#FFBB33",
            lowPriorityColor = "#00C851",
            onAutoWallpaperToggle = {},
            onWallpaperColorClick = {},
            onSecondaryWallpaperColorClick = {},
            onPriorityColorClick = {},
            onDarkModeChange = {},
            onSyncWallpaper = {},
            onNotificationPermissionClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel? = null,
) {
    if (LocalInspectionMode.current) {
        SettingsPreviewContent()
        return
    }
    val vm = viewModel ?: hiltViewModel()

    val isAutoWallpaperEnabled by vm.autoWallpaperEnabled.collectAsState()
    val currentWallpaperColor by vm.wallpaperColor.collectAsState()
    val secondaryWallpaperColor by vm.wallpaperSecondaryColor.collectAsState()
    val currentDarkMode by vm.darkMode.collectAsState()
    val highPriorityColor by vm.highPriorityColor.collectAsState()
    val mediumPriorityColor by vm.mediumPriorityColor.collectAsState()
    val lowPriorityColor by vm.lowPriorityColor.collectAsState()
    val isNotificationEnabled = vm.notificationPermissionState

    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { vm.updateNotificationPermissionState() }

    if (vm.showColorPickerDialog) {
        ColorPickerDialog(
            initialColor = vm.selectedColor,
            title = when (vm.currentColorPickerMode) {
                ColorPickerMode.WALLPAPER -> stringResource(R.string.choose_wallpaper_color)
                ColorPickerMode.SECONDARY_WALLPAPER -> stringResource(R.string.choose_secondary_wallpaper_color)
                ColorPickerMode.HIGH_PRIORITY -> stringResource(R.string.choose_high_priority_color)
                ColorPickerMode.MEDIUM_PRIORITY -> stringResource(R.string.choose_medium_priority_color)
                ColorPickerMode.LOW_PRIORITY -> stringResource(R.string.choose_low_priority_color)
            },
            onColorSelected = { color ->
                vm.updateSelectedColor(color)
                vm.applySelectedColor()
            },
            onDismiss = { vm.hideColorPicker() }
        )
    }

    LaunchedEffect(Unit) {
        vm.updateNotificationPermissionState()
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(stringResource(R.string.notification_permission_title)) },
            text = { Text(stringResource(R.string.notification_permission_message)) },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }) {
                    Text(stringResource(R.string.grant_permission))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(stringResource(R.string.not_now))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) })
        }
    ) { padding ->
        SettingsContent(
            modifier = Modifier.padding(padding),
            isAutoWallpaperEnabled = isAutoWallpaperEnabled,
            currentWallpaperColor = currentWallpaperColor,
            secondaryWallpaperColor = secondaryWallpaperColor,
            currentDarkMode = currentDarkMode,
            isNotificationEnabled = isNotificationEnabled,
            highPriorityColor = highPriorityColor,
            mediumPriorityColor = mediumPriorityColor,
            lowPriorityColor = lowPriorityColor,
            onAutoWallpaperToggle = { vm.setAutoWallpaperEnabled(it) },
            onWallpaperColorClick = { vm.showWallpaperColorPicker() },
            onSecondaryWallpaperColorClick = { vm.showSecondaryWallpaperColorPicker() },
            onPriorityColorClick = { priority -> vm.showPriorityColorPicker(priority) },
            onDarkModeChange = { vm.setDarkMode(it) },
            onSyncWallpaper = { vm.updateWallpaper() },
            onNotificationPermissionClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationEnabled) {
                    showPermissionDialog = true
                }
            }
        )
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    isAutoWallpaperEnabled: Boolean,
    currentWallpaperColor: String,
    secondaryWallpaperColor: String,
    currentDarkMode: String,
    isNotificationEnabled: Boolean,
    highPriorityColor: String,
    mediumPriorityColor: String,
    lowPriorityColor: String,
    onAutoWallpaperToggle: (Boolean) -> Unit,
    onWallpaperColorClick: () -> Unit,
    onSecondaryWallpaperColorClick: () -> Unit,
    onPriorityColorClick: (Priority) -> Unit,
    onDarkModeChange: (String) -> Unit,
    onSyncWallpaper: () -> Unit,
    onNotificationPermissionClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        SectionCard {
            SettingSwitchItem(
                title = stringResource(R.string.auto_wallpaper_updates),
                subtitle = stringResource(R.string.auto_wallpaper_subtitle),
                checked = isAutoWallpaperEnabled,
                onCheckedChange = onAutoWallpaperToggle
            )

            SectionDivider()

            Text(
                text = stringResource(R.string.wallpaper_color),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.wallpaper_color_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                WallpaperColorCircle(
                    colorHex = currentWallpaperColor,
                    label = stringResource(R.string.primary),
                    onClick = onWallpaperColorClick
                )
                WallpaperColorCircle(
                    colorHex = secondaryWallpaperColor,
                    label = stringResource(R.string.secondary),
                    onClick = onSecondaryWallpaperColorClick
                )
            }
        }

        SectionHeader(title = stringResource(R.string.task_priority_colors))

        SectionCard {
            ColorSettingItem(
                title = stringResource(R.string.high_priority),
                subtitle = stringResource(R.string.high_priority_subtitle),
                colorHex = highPriorityColor,
                onClick = { onPriorityColorClick(Priority.HIGH) }
            )
            SectionDivider()
            ColorSettingItem(
                title = stringResource(R.string.medium_priority),
                subtitle = stringResource(R.string.medium_priority_subtitle),
                colorHex = mediumPriorityColor,
                onClick = { onPriorityColorClick(Priority.MEDIUM) }
            )
            SectionDivider()
            ColorSettingItem(
                title = stringResource(R.string.low_priority),
                subtitle = stringResource(R.string.low_priority_subtitle),
                colorHex = lowPriorityColor,
                onClick = { onPriorityColorClick(Priority.LOW) }
            )
        }

        SectionHeader(title = stringResource(R.string.appearance))

        SectionCard {
            DarkModeOption(
                label = stringResource(R.string.dark_mode_system),
                selected = currentDarkMode == "system",
                onClick = { onDarkModeChange("system") }
            )
            SectionDivider()
            DarkModeOption(
                label = stringResource(R.string.dark_mode_light),
                selected = currentDarkMode == "light",
                onClick = { onDarkModeChange("light") }
            )
            SectionDivider()
            DarkModeOption(
                label = stringResource(R.string.dark_mode_dark),
                selected = currentDarkMode == "dark",
                onClick = { onDarkModeChange("dark") }
            )
        }

        SectionHeader(title = stringResource(R.string.notifications))

        SectionCard {
            SettingSwitchItem(
                title = stringResource(R.string.permission),
                subtitle = stringResource(R.string.enable_notification_permission),
                checked = isNotificationEnabled,
                onCheckedChange = { onNotificationPermissionClick() }
            )
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSyncWallpaper,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sync),
                contentDescription = stringResource(R.string.sync_up_content_desc),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.sync_up))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
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
            modifier = Modifier.fillMaxWidth(),
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
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    controller = controller,
                    initialColor = initialColor,
                    onColorChanged = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    controller = controller
                )
                Spacer(modifier = Modifier.height(16.dp))
                AlphaTile(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    controller = controller
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(onClick = { onColorSelected(controller.selectedColor.value) }) {
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
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
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
                .size(44.dp)
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
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
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
