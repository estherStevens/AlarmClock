package stevens.software.alarmclock.ui.alarms

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmTime
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.ui.create_alarm.CreateAlarmUiState
import java.time.LocalTime

class AlarmsViewModel(
    val alarmsRepository: AlarmsRepository,
    val alarmSchedulerRepository: AlarmSchedulerRepository): ViewModel() {

    private val isLoading = MutableStateFlow<Boolean>(true)

    val uiState = combine(
        alarmsRepository.getAllAlarms(),
        isLoading
    ) { alarms, isLoading ->
        AlarmsUiState(
            alarms = alarms,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AlarmsUiState(
            alarms = emptyList(),
            isLoading = true
        )
    )

    fun updateAlarm(alarmId: Int, isEnabled: Boolean){
        viewModelScope.launch{
            alarmsRepository.updateAlarm(alarmId, isEnabled)
            val alarm = alarmsRepository.getAlarm(alarmId)
            alarm.collect { it ->
                when(it.enabled){
                    true -> {
                        alarmSchedulerRepository.scheduleAlarm(
                            alarmId = it.id,
                            alarmName = it.name,
                            alarmTime = AlarmTime(alarmHour = it.hour, alarmMinute = it.minute)
                        )
                    }
                    false -> {
                        alarmSchedulerRepository.cancelAlarm(
                            alarmName = it.name,
                            alarmTime = AlarmTime(alarmHour = it.hour, alarmMinute = it.minute)
                        )
                    }
                }
//                alarmsRepository.updateAlarm()
            }
        }

    }




//    val uiState = MutableStateFlow<AlarmsUiState>(
//        AlarmsUiState(
//            alarms = mockAlarms()
//        )
//    ).onStart {
//        alarmsRepository.getAllAlarms()
//    }


//
//    val i = alarmsRepository.getAllAlarms().collect{
//
//    }
//    val uiStatee: MutableStateFlow<AlarmsUiState> = alarmsRepository.getAllAlarms().map {
//        AlarmsUiState(
//
//        )
//
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.Eagerly,
//        AlarmsUiState(alarms = emptyList())
//    )


//    val uiState = alarmsRepository.getAllAlarms().map{
//
//    }
//
    init {
//        viewModelScope.launch{
//
//           val o =  alarmsRepository.getAllAlarms()
//            o.collect{
//                    value -> println("Collected $value")
//            }
//        }
//        alarmsRepository.getAllAlarms()
    }

//    fun mockAlarms() = emptyList<AlarmDto>()
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

//
//    fun Alarm.toCreateAlarmUiState() =
//        AlarmsUiState(
//            alarms = this,
//        )

}

data class AlarmsUiState(
    val alarms: List<Alarm>,
    val isLoading: Boolean
)

//data class AlarmDto(
//    val time: String,
//    val selectedDays: List<SelectedDaysOfTheWeek>
//)



enum class SelectedDaysOfTheWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}