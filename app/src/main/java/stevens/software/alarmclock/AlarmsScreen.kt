package stevens.software.alarmclock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AlarmsScreen() {
    Alarms()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Alarms(
    alarmsViewModel: AlarmsViewModel = viewModel(),
) {
    val uiState = alarmsViewModel.uiState.collectAsStateWithLifecycle()
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
                fontWeight = FontWeight.Bold,
            )

            if(uiState.value.alarms.isEmpty()){
                EmptyState(modifier = Modifier.weight(1f))
            } else {

                LazyColumn(modifier = Modifier.weight(1f)) {

                    items(uiState.value.alarms) { alarm ->
                        AlarmCard(
                            alarmTime = alarm.time,
                            selectedDays = alarm.selectedDays
                        )
                    }
                }

            }
//
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, top = 16.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                FloatingActionButton(
                    onClick = {},
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
@Preview(showSystemUi = true)
fun AlarmsScreenPreview() {
    MaterialTheme {
        Alarms()
    }
}

@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    alarmTime: String,
    selectedDays: List<SelectedDaysOfTheWeek>
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.white))
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Column {
            Text(
                text = stringResource(id = R.string.wake_up),
                fontSize = 16.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_black)
            )
            Spacer(Modifier.size(10.dp))
            Text(
                text = alarmTime,
                fontSize = 42.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_black)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Alarm in 30 min",
                fontSize = 14.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )
            Spacer(Modifier.size(8.dp))
            AlarmDayPills(selectedDays = selectedDays)
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Go to bed at 02:00AM to get 8h of sleep",
                fontSize = 14.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey)
            )

        }
    }
}

@Composable
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
}

@Composable
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
}

@Composable
fun EmptyState(modifier: Modifier){
    Box(modifier = modifier.padding(horizontal = 31.dp),
        contentAlignment = Alignment.Center) {
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
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_black),
                textAlign = TextAlign.Center
            )
        }
    }
}

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular)
)