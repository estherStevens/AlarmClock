package stevens.software.alarmclock.ui.alarms

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.text.util.LocalePreferences
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Duration

import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.HOURS
import java.time.temporal.ChronoUnit.MINUTES
import java.time.temporal.ChronoUnit.MONTHS
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlarmsViewModel(
    val alarmsRepository: AlarmsRepository,
    val alarmSchedulerRepository: AlarmSchedulerRepository): ViewModel() {

    private val isLoading = MutableStateFlow<Boolean>(true)
    private val timeRemaining = MutableStateFlow<LocalTime?>(null)

    @RequiresApi(Build.VERSION_CODES.S)
    val uiState = combine(
        alarmsRepository.getAllAlarms(),
        isLoading,
    ) { alarms, isLoading ->
        AlarmsUiState(
            alarms = alarms.map { it.toAlarmDto() },
            isLoading = false,
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
                            alarmTime = it.alarmTime
                        )
                    }
                    false -> {
                        alarmSchedulerRepository.cancelAlarm(
                            alarmId = it.id,
                            alarmName = it.name,
                            alarmTime = AlarmTime(
                                alarmHour = it.alarmTime.hour.toString(),
                                alarmMinute = it.alarmTime.minute.toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun deleteAlarm(alarm: AlarmDto){
        viewModelScope.launch{
            alarmSchedulerRepository.cancelAlarm(
                alarmId = alarm.id,
                alarmName = alarm.name,
                alarmTime = AlarmTime(
                    alarmHour = alarm.alarmTime.hour.toString(),
                    alarmMinute = alarm.alarmTime.minute.toString()
                )

            )
            alarmsRepository.deleteAlarm(alarm.toAlarm())
        }
    }

    fun AlarmDto.toAlarm() : Alarm {
        return Alarm(
            id = this.id,
            name = this.name,
            enabled = this.enabled,
            alarmTime = this.alarmTime
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Alarm.toAlarmDto() : AlarmDto {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmTime.hour.toInt())
            set(Calendar.MINUTE, alarmTime.minute.toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val a = LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
        val elapsed = Duration.between(LocalDateTime.now(), a)

        val hoursRemaining = elapsed.toHours().toFloat()
        val minutesRemaining = elapsed.toMinutesPart()

        return AlarmDto(
            id = this.id,
            name = this.name,
            enabled = this.enabled,
            timeRemaining = RemainingTime(hours = hoursRemaining.toInt(), minutes = minutesRemaining),
            alarmTime = this.alarmTime
        )
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
//    init {
//        viewModelScope.launch{
//
//           val o =  alarmsRepository.getAllAlarms()
//            o.collect{
//                    value -> println("Collected $value")
//            }
//        }
//        alarmsRepository.getAllAlarms()
//    }

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
    val alarms: List<AlarmDto>,
    val isLoading: Boolean,
)


data class AlarmDto(
    val id: Int = 0,
    val name: String,
    val alarmTime: LocalDateTime,
    val enabled: Boolean,
    val timeRemaining: RemainingTime
)

data class RemainingTime(val hours: Int, val minutes: Int)
enum class SelectedDaysOfTheWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}