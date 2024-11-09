package stevens.software.alarmclock

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AlarmsViewModel: ViewModel() {

    val uiState = MutableStateFlow<AlarmsUiState>(
        AlarmsUiState(
            alarms = mockAlarms()
        )
    )

    fun mockAlarms() = listOf<Alarm>(
        Alarm(
            time = "10:00",
            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
        ),
        Alarm(
            time ="13:00",
            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
        ),
        Alarm(
            time = "10:00",
            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
        ),
        Alarm(
            time = "10:00",
            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
        ),
        Alarm(
            time = "10:00",
            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
        ),
    )
}

data class AlarmsUiState(val alarms: List<Alarm>)

data class Alarm(
    val time: String,
    val selectedDays: List<SelectedDaysOfTheWeek>
)



enum class SelectedDaysOfTheWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}