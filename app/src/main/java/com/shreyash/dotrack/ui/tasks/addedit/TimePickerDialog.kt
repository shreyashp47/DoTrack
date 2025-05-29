package com.shreyash.dotrack.ui.tasks.addedit

import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@Composable
fun TimePickerDialog(
    selectedDate: LocalDate,
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var hour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Clock-style TimePicker (12-hour view)
                AndroidView(
                    factory = {
                        TimePicker(context).apply {
                            setIs24HourView(false)
                            this.hour = hour
                            this.minute = minute
                            setOnTimeChangedListener { _, newHour, newMinute ->
                                hour = newHour
                                minute = newMinute
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Formatted selected time
                val formattedTime = remember(hour, minute) {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
                }

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    Button(onClick = {
                        val now = Calendar.getInstance()

                        // Create calendar from selected date and time
                        val selectedDateTime = Calendar.getInstance().apply {
                            set(Calendar.YEAR, selectedDate.year)
                            set(Calendar.MONTH, selectedDate.monthValue - 1)
                            set(Calendar.DAY_OF_MONTH, selectedDate.dayOfMonth)
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }

                        if (selectedDateTime.before(now)) {
                            Toast.makeText(
                                context,
                                "Cannot select past time",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onTimeSelected(hour, minute)
                            onDismissRequest()
                        }
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TimePickerDialogPreview() {
    TimePickerDialog(
        selectedDate = LocalDate.now(),
        onDismissRequest = {},
        onTimeSelected = { _, _ -> }
    )
}