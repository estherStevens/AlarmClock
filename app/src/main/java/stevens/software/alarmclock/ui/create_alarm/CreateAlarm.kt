package stevens.software.alarmclock.ui.create_alarm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import stevens.software.alarmclock.R
import stevens.software.alarmclock.montserratFontFamily
import stevens.software.alarmclock.ui.alarms.RemainingTime

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CreateAlarmScreen(
    createAlarmViewModel: CreateAlarmViewModel = koinViewModel(),
    onCloseButtonClicked: () -> Unit,
    onSaveAlarmSuccess: () -> Unit,
) {
    val uiState = createAlarmViewModel.uiState.collectAsStateWithLifecycle()

    CreateAlarm(
        uiState = uiState.value,
        onCloseButtonClicked = onCloseButtonClicked,
        onSaveAlarmSuccess = onSaveAlarmSuccess,
        onUpdateAlarmName = { createAlarmViewModel.updateAlarmName(it) },
        onUpdateAlarmHour = { createAlarmViewModel.updateAlarmHour(it) },
        onUpdateAlarmMinute = { createAlarmViewModel.updateAlarmMinute(it) },
        onSaveAlarm = { createAlarmViewModel.saveAlarm() },
        )
}

@Composable
fun CreateAlarm(
    uiState: CreateAlarmUiState,
    onCloseButtonClicked: () -> Unit = {},
    onSaveAlarmSuccess: () -> Unit = {},
    onUpdateAlarmName: (String) -> Unit = {},
    onUpdateAlarmMinute: (String) -> Unit = {},
    onUpdateAlarmHour: (String) -> Unit = {},
    onSaveAlarm: () -> Unit = {},
) {
    var openChangeAlarmNameDialog = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successSavingAlarm) {
        if (uiState.successSavingAlarm == true) {
            onSaveAlarmSuccess()
        }
    }

    if (openChangeAlarmNameDialog.value) {
        AddAlarmNameDialog(
            alarmName = uiState.alarmName,
            onSaveClicked = {
                onUpdateAlarmName(it)
                openChangeAlarmNameDialog.value = false
            },
            onDismissDialog = {
                openChangeAlarmNameDialog.value = false
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.close_button),
                    contentDescription = stringResource(R.string.close_button),
                    modifier = Modifier
                        .clickable {
                            onCloseButtonClicked()
                        },
                    tint = colorResource(R.color.light_grey)
                )

                Button(
                    onClick = onSaveAlarm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.saveButtonEnabled) colorResource(R.color.blue) else colorResource(
                            R.color.light_grey
                        )
                    ),
                    enabled = uiState.saveButtonEnabled
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        color = colorResource(R.color.white),
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.size(24.dp))
            AlarmTimePicker(
                hour = uiState.alarmHour,
                minute = uiState.alarmMinute,
                timeRemaining = uiState.timeRemaining,
                onHourChanged = onUpdateAlarmHour,
                onMinuteChanged = onUpdateAlarmMinute,
            )

            Spacer(Modifier.size(16.dp))
            AlarmName(
                alarmNameValue = uiState.alarmName,
                onAlarmNameClicked = {
                    openChangeAlarmNameDialog.value = true
                }
            )
            Spacer(Modifier.size(16.dp))

        }
    }
}

@Composable
fun AlarmTimePicker(
    hour: String,
    minute: String,
    timeRemaining: RemainingTime?,
    onHourChanged: (String) -> Unit,
    onMinuteChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.white))
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HourTimePicker(
                    alarmHour = hour,
                    onHourChanged = { it ->
                        onHourChanged(it)
                    },
                )
                Text(
                    text = ":",
                    color = colorResource(R.color.grey),
                    fontSize = 52.sp,
                    fontFamily = montserratFontFamily,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                MinuteTimePicker(
                    alarmMinute = minute,
                    onMinuteChanged = { it ->
                        onMinuteChanged(it)
                    }
                )
            }

            if(timeRemaining?.hours != null && timeRemaining?.minutes != null) {
                Spacer(Modifier.size(16.dp))
                Text(
                    text = getTimeRemainingString(
                        timeRemaining.hours,
                        timeRemaining.minutes
                    ),
                    fontFamily = montserratFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = colorResource(R.color.grey)
                )
            }
        }
    }

}

fun getTimeRemainingString(hour: Int, minute: Int): String {
    val hoursRemaining = if (hour == 0) "" else "${hour}h "
    val minutesRemaining = if (minute == 0) "> 1 min" else "${minute}mins"
    return "Alarm in $hoursRemaining$minutesRemaining"
}


@Composable
fun HourTimePicker(alarmHour: String, onHourChanged: (String) -> Unit) {
    val regex = Regex("^(\\s*|([0-9]|0[0-9]|1[0-9]|2[0-3]))\$")
    TimePickerItem(
        value = alarmHour,
        regex = regex,
        onValueChanged = onHourChanged
    )
}

@Composable
fun MinuteTimePicker(alarmMinute: String, onMinuteChanged: (String) -> Unit) {
    val regex = Regex("^(\\s*|([0-9]|0[0-9]|[1-5][0-9]))$")
    TimePickerItem(
        value = alarmMinute,
        regex = regex,
        onValueChanged = onMinuteChanged
    )
}

@Composable
fun TimePickerItem(value: String, regex: Regex, onValueChanged: (String) -> Unit) {
    var value by remember { mutableStateOf(value) }
    TextField(
        value = value,
        onValueChange = {
            if (it.matches(regex)) {
                value = it
                onValueChanged(it)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.width(128.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colorResource(R.color.blue),
            unfocusedTextColor = colorResource(R.color.blue),
            focusedPlaceholderColor = colorResource(R.color.grey),
            unfocusedPlaceholderColor = colorResource(R.color.grey),
            focusedContainerColor = colorResource(R.color.very_light_grey),
            unfocusedContainerColor = colorResource(R.color.very_light_grey),
        ),
        placeholder = {
            Text(
                text = "00",
                fontSize = 52.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        textStyle = TextStyle.Default.copy(
            fontSize = 52.sp,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        ),
    )
}

@Composable
fun AlarmName(alarmNameValue: String, onAlarmNameClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.white))
            .clickable {
                onAlarmNameClicked()
            }
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.alarm_name),
                color = colorResource(R.color.dark_black),
                fontFamily = montserratFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = alarmNameValue,
                color = colorResource(R.color.grey),
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AddAlarmNameDialog(
    alarmName: String,
    onDismissDialog: () -> Unit,
    onSaveClicked: (String) -> Unit
) {
    var alarmName by remember {
        mutableStateOf(alarmName)
    }

    Dialog(
        onDismissRequest = onDismissDialog,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = colorResource(R.color.white)
            ),
            shape = RoundedCornerShape(10.dp),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.alarm_name),
                    fontFamily = montserratFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.size(10.dp))
            OutlinedTextField(
                value = alarmName,
                onValueChange = {
                    alarmName = it
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.light_grey),
                    unfocusedBorderColor = colorResource(R.color.light_grey)
                ),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(R.color.black),
                    fontFamily = montserratFontFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.size(10.dp))

            Button(
                onClick = {
                    onSaveClicked(alarmName)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(R.color.blue),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text(
                    text = stringResource(R.string.save_alarm_name),
                    fontFamily = montserratFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold

                )
            }

        }
    }
}
