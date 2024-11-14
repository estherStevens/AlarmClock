package stevens.software.alarmclock.ui.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import java.time.LocalTime

class AlarmsViewModel(val alarmsRepository: AlarmsRepository): ViewModel() {

    val uiState = MutableStateFlow<AlarmsUiState>(
        AlarmsUiState(
            alarms = mockAlarms()
        )
    )
//
    init {
        viewModelScope.launch{

           val o =  alarmsRepository.getAllAlarms()
            o.collect{
                    value -> println("Collected $value")
            }
        }
//        alarmsRepository.getAllAlarms()
    }

    fun mockAlarms() = emptyList<AlarmDto>()
//        listOf<Alarm>(
//        Alarm(
//            time = "10:00",
//            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
//        ),
//        Alarm(
//            time ="13:00",
//            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
//        ),
//        Alarm(
//            time = "10:00",
//            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
//        ),
//        Alarm(
//            time = "10:00",
//            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
//        ),
//        Alarm(
//            time = "10:00",
//            selectedDays = listOf(SelectedDaysOfTheWeek.MONDAY, SelectedDaysOfTheWeek.TUESDAY)
//        ),
//    )
}

data class AlarmsUiState(val alarms: List<AlarmDto>)

data class AlarmDto(
    val time: String,
    val selectedDays: List<SelectedDaysOfTheWeek>
)



enum class SelectedDaysOfTheWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}