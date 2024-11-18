package stevens.software.alarmclock.ui.triggeredAlarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import stevens.software.alarmclock.R
import stevens.software.alarmclock.data.AlarmTime
import stevens.software.alarmclock.ui.alarms.montserratFontFamily

@Composable
fun TriggeredAlarmScreen(
    viewModel: TriggeredAlarmViewModel,
    onTurnOffClicked: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    TriggeredAlarm(
        alarmTime = uiState.value.alarmTime,
        alarmName = uiState.value.alarmName,
        onTurnOffClicked = {
            viewModel.turnOffAlarm()
            onTurnOffClicked()
        }
    )
}

@Composable
fun TriggeredAlarm(
    alarmTime: AlarmTime,
    alarmName: String,
    onTurnOffClicked: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.empty_state_icon),
                contentDescription = ""
            )
            Spacer(Modifier.size(24.dp))
            Text(
                text = "${alarmTime.alarmHour}:${alarmTime.alarmMinute}",
                fontFamily = montserratFontFamily,
                fontSize = 82.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.blue),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(10.dp))
            Text(
                text = alarmName,
                fontFamily = montserratFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.dark_black),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = onTurnOffClicked,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(R.color.blue),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text(
                    text = stringResource(R.string.turn_off_alarm),
                    fontFamily = montserratFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

        }
    }

}

//
//@Preview(showSystemUi = true)
//@Composable
//fun PreviewTriggeredAlarm(){
//    MaterialTheme{
//        TriggeredAlarmScreen(
//            alarmName = "Work",
//            alarmTime = "12:00",
//            onTurnOffClicked = {}
//        )
//    }
//}