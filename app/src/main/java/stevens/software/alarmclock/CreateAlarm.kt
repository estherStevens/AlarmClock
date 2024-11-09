package stevens.software.alarmclock

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateAlarmScreen(onCloseButtonClicked: () -> Unit){
    CreateAlarm(onCloseButtonClicked = onCloseButtonClicked)
}

@Composable
fun CreateAlarm(
    createAlarmViewModel: CreateAlarmViewModel = viewModel(),
    onCloseButtonClicked: () -> Unit = {},
) {
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
                        containerColor = colorResource(R.color.light_grey)
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        color = colorResource(R.color.white)
                    )
                }
            }
            Spacer(Modifier.size(24.dp))
            AlarmTimePicker()
            Spacer(Modifier.size(16.dp))
            AlarmName(alarmNameValue = "Work")
            Spacer(Modifier.size(16.dp))
//            AddAlarmNameDialog("Work", {})
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTimePicker() {
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
            TimePicker(
                value = "00",
                onValueChange = {}
            )
            Text(
                text = ":",
                color = colorResource(R.color.grey),
                fontSize = 52.sp,
                fontFamily = montserratFontFamily,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            TimePicker(
                value = "00",
                onValueChange = {}
            )
        }
    }

}

@Composable
fun TimePicker(value: String, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf(value) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier.width(128.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = colorResource(R.color.very_light_grey),
            unfocusedContainerColor = colorResource(R.color.very_light_grey),
        ),
        textStyle = TextStyle.Default.copy(
            fontSize = 52.sp,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.grey),
            textAlign = TextAlign.Center,
        ),
    )
}


@Composable
fun AlarmName(alarmNameValue: String){
    var alarmNameValue by remember { mutableStateOf(alarmNameValue) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.white))
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
fun AddAlarmNameDialog(alarmName: String, onAlarmNameChanged: (String) -> Unit){
    var alarmName by remember { mutableStateOf(alarmName) }

    Dialog(
        onDismissRequest = {}
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
                onValueChange = onAlarmNameChanged,
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
                onClick = {},
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
