package stevens.software.alarmclock

import android.widget.NumberPicker
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateAlarmScreen(onCloseButtonClicked: () -> Unit){
    CreateAlarm(
        onCloseButtonClicked = onCloseButtonClicked
    )
}

@Composable
fun CreateAlarm(
    createAlarmViewModel: CreateAlarmViewModel = viewModel(),
    onCloseButtonClicked: () -> Unit = {}
) {
    val uiState = createAlarmViewModel.uiState.collectAsStateWithLifecycle()
    var openChangeAlarmNameDialog = remember { mutableStateOf(false) }
    var saveButtonEnabled = uiState.value.saveButtonEnabled
    var alarmName = uiState.value.alarmName

    if(openChangeAlarmNameDialog.value) {
        AddAlarmNameDialog(
            alarmName = alarmName,
            onSaveClicked = {
                createAlarmViewModel.updateAlarmName(it)
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
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(saveButtonEnabled) colorResource(R.color.blue) else colorResource(R.color.light_grey)
                    ),
                    enabled = saveButtonEnabled
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        color = colorResource(R.color.white)
                    )
                }
            }
            Spacer(Modifier.size(24.dp))
            AlarmTimePicker(
                hour = uiState.value.alarmHour,
                minute = uiState.value.alarmMinute,
                onHourChanged = {
                    createAlarmViewModel.updateAlarmHour(it)
                },
                onMinuteChanged = {
                    createAlarmViewModel.updateAlarmMinute(it)
                },
            )
            Spacer(Modifier.size(16.dp))
            AlarmName(
                alarmNameValue = uiState.value.alarmName,
                onAlarmNameClicked = {
                    openChangeAlarmNameDialog.value = true
                }
            )
            Spacer(Modifier.size(16.dp))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTimePicker(hour: String,
                    minute: String,
                    onHourChanged: (String) -> Unit,
                    onMinuteChanged: (String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.white))
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
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
    }

}

@Composable
fun HourTimePicker(alarmHour: String, onHourChanged: (String) -> Unit){
    val regex = Regex("^(\\s*|([0-9]|0[0-9]|1[0-9]|2[0-3]))\$")
    TimePickerItem(
        value = alarmHour,
        regex = regex,
        onValueChanged = onHourChanged
    )
}

@Composable
fun MinuteTimePicker(alarmMinute: String, onMinuteChanged: (String) -> Unit){
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
            if(it.matches(regex)) {
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
            focusedPlaceholderColor = colorResource(R.color.blue),
            unfocusedPlaceholderColor = colorResource(R.color.grey),
            focusedContainerColor = colorResource(R.color.very_light_grey),
            unfocusedContainerColor = colorResource(R.color.very_light_grey),
        ),
        placeholder = {
            Text(
                text = "00",
                fontSize = 52.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        textStyle = TextStyle.Default.copy(
            fontSize = 52.sp,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        ),
    )
}

@Composable
fun AlarmName(alarmNameValue: String, onAlarmNameClicked: () -> Unit){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.white))
            .clickable {
                onAlarmNameClicked()
            }
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Text(
                text = stringResource(R.string.alarm_name),
                color = colorResource(R.color.dark_black),
                fontFamily = montserratFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = alarmNameValue,
                color = colorResource(R.color.grey),
                fontFamily = montserratFontFamily,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AddAlarmNameDialog(alarmName: String, onDismissDialog: () -> Unit, onSaveClicked: (String) -> Unit){
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
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
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
                modifier = Modifier.align(Alignment.End).padding(end = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(R.color.blue),
                    contentColor = colorResource(R.color.white)
                )
            ){
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

@Preview(showSystemUi = true)
@Composable
fun CreateAlarmPreview() {
    MaterialTheme {
        CreateAlarm()
    }
}
