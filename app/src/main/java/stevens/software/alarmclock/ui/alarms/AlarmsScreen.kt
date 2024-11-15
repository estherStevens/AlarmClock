package stevens.software.alarmclock.ui.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue




import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import stevens.software.alarmclock.R
import stevens.software.alarmclock.data.Alarm


@Composable
fun AlarmsScreen(
    onAddAlarmClicked: () -> Unit,
    alarmsViewModel: AlarmsViewModel = koinViewModel()
) {
    val uiState = alarmsViewModel.uiState.collectAsStateWithLifecycle()
    Alarms(
        alarms = uiState.value.alarms,
        isLoading = uiState.value.isLoading,
        onAddAlarmClicked = onAddAlarmClicked,
        onAlarmToggled = { id, isEnabled ->
            alarmsViewModel.updateAlarm(id, isEnabled)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Alarms(
    alarms: List<Alarm>,
    isLoading: Boolean,
    onAddAlarmClicked: () -> Unit,
    onAlarmToggled: (Int, Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.your_alarms),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                fontSize = 24.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium,
            )

            if(isLoading) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = colorResource(R.color.blue)
                    )
                }

            } else {
                if (alarms.isEmpty()) {
                    EmptyState(modifier = Modifier.weight(1f))
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(alarms) { alarm ->
                            AlarmCard(
                                alarmId = alarm.id,
                                alarmName = alarm.name,
                                alarmHour = alarm.hour,
                                alarmMinute = alarm.minute,
                                alarmEnabled = alarm.enabled,
                                onAlarmToggled = onAlarmToggled
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, top = 16.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                FloatingActionButton(
                    onClick = onAddAlarmClicked,
                    shape = CircleShape,
                    containerColor = colorResource(R.color.blue),
                    contentColor = colorResource(R.color.white)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}


@Composable
fun AlarmCard(
    alarmId: Int,
    alarmName: String,
    alarmHour: String,
    alarmMinute: String,
    alarmEnabled: Boolean,
    onAlarmToggled: (Int, Boolean) -> Unit
    /*electedDays: List<SelectedDaysOfTheWeek>*/
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.white))
            .padding(top = 10.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {

        Column{
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {

                Text(
                    text = alarmName,
                    fontSize = 16.sp,
                    fontFamily = montserratFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.dark_black),
                )
                AlarmSwitch(
                    alarmEnabled = alarmEnabled,
                    onAlarmToggled = { isEnabled ->
                        onAlarmToggled(
                            alarmId,
                            isEnabled
                        )
                    }
                )
            }

            Text(
                text = "$alarmHour:$alarmMinute" ,
                fontSize = 42.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.dark_black)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Alarm in 30 min",
                fontSize = 14.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.grey)
            )
            /*Spacer(Modifier.size(8.dp))*/
            /*AlarmDayPills(selectedDays = selectedDays)*/
            /*Spacer(Modifier.size(8.dp))*/
            /*Text(
                text = "Go to bed at 02:00AM to get 8h of sleep",
                fontSize = 14.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )*/

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmSwitch(
    alarmEnabled: Boolean,
    onAlarmToggled: (Boolean) -> Unit
){
    var checked by remember { mutableStateOf(alarmEnabled) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
            onAlarmToggled(it)
        },
        modifier = Modifier.padding(top = 6.dp),
        colors = SwitchDefaults.colors().copy(
            checkedTrackColor = colorResource(R.color.blue),
            uncheckedTrackColor = colorResource(R.color.very_light_blue),
            uncheckedThumbColor = colorResource(R.color.white),
            uncheckedBorderColor = colorResource(R.color.very_light_blue)
        )
    )

}
/*@Composable
fun AlarmDayPills(selectedDays: List<SelectedDaysOfTheWeek>) {
    var isMondaySelected: Boolean = false
    var isTuesdaySelected: Boolean = false
    var isWednesdaySelected: Boolean = false
    var isThursdaySelected: Boolean = false
    var isFridaySelected: Boolean = false
    var isSaturdaySelected: Boolean = false
    var isSundaySelected: Boolean = false

    for (day in selectedDays) {
        when (day) {
            SelectedDaysOfTheWeek.MONDAY -> isMondaySelected = true
            SelectedDaysOfTheWeek.TUESDAY -> isTuesdaySelected = true
            SelectedDaysOfTheWeek.WEDNESDAY -> isWednesdaySelected = true
            SelectedDaysOfTheWeek.THURSDAY -> isThursdaySelected = true
            SelectedDaysOfTheWeek.FRIDAY -> isFridaySelected = true
            SelectedDaysOfTheWeek.SATURDAY -> isSaturdaySelected = true
            SelectedDaysOfTheWeek.SUNDAY -> isSundaySelected = true
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AlarmDayPill("Mo", isMondaySelected)
        AlarmDayPill("Tu", isTuesdaySelected)
        AlarmDayPill("We", isWednesdaySelected)
        AlarmDayPill("Th", isThursdaySelected)
        AlarmDayPill("Fr", isFridaySelected)
        AlarmDayPill("Sa", isSaturdaySelected)
        AlarmDayPill("Su", isSundaySelected)
    }
}*/

/*@Composable
fun AlarmDayPill(
    dayOfWeek: String,
    isPillSelected: Boolean
) {
    val pillColour =
        if (isPillSelected) colorResource(R.color.blue) else colorResource(R.color.light_blue)
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 38.dp)
            .clip(RoundedCornerShape(38.dp))
            .background(color = pillColour)
    ) {
        Text(
            text = dayOfWeek,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            color = colorResource(R.color.white),
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}*/

@Composable
fun EmptyState(modifier: Modifier) {
    Box(
        modifier = modifier.padding(horizontal = 31.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.empty_state_icon),
                contentDescription = ""
            )
            Spacer(Modifier.size(32.dp))
            Text(
                text = stringResource(R.string.empty_state_text),
                fontFamily = montserratFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.dark_black),
                textAlign = TextAlign.Center
            )
        }
    }
}

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_medium, FontWeight.Medium)

)

@Composable
@Preview(showSystemUi = true)
fun AlarmsScreenPreview() {
    MaterialTheme {
        Alarms (
            alarms = listOf(Alarm(name = "", hour = "00", minute = "00", enabled = true)),
            isLoading = false,
            onAddAlarmClicked = { },
            onAlarmToggled = {_,_ -> }
        )
    }
}