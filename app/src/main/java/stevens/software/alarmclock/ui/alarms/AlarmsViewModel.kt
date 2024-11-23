package stevens.software.alarmclock.ui.alarms

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import java.time.LocalDateTime
import java.time.Duration

import java.util.Calendar

class AlarmsViewModel(
    val alarmsRepository: AlarmsRepository,
    val alarmSchedulerRepository: AlarmSchedulerRepository
) : ViewModel() {

    private val isLoading = MutableStateFlow<Boolean>(true)

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

    fun updateAlarmEnabledState(alarmId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            alarmsRepository.updateAlarm(alarmId, isEnabled)
            val alarm = alarmsRepository.getAlarm(alarmId)
            alarm.collect { it ->
                when (it.enabled) {
                    true -> {
                        alarmSchedulerRepository.scheduleAlarm(
                            alarmId = it.id,
                            alarmName = it.name,
                            alarmTime = it.alarmTime,
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

    fun deleteAlarm(alarm: AlarmDto) {
        viewModelScope.launch {
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

    fun AlarmDto.toAlarm(): Alarm {
        return Alarm(
            id = this.id,
            name = this.name,
            enabled = this.enabled,
            alarmTime = this.alarmTime,
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Alarm.toAlarmDto(): AlarmDto {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmTime.hour.toInt())
            set(Calendar.MINUTE, alarmTime.minute.toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val elapsedTime = Duration.between(
            LocalDateTime.now(),
            LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
        )

        val hoursRemaining = elapsedTime.toHours().toFloat()
        val minutesRemaining = elapsedTime.toMinutesPart()

        return AlarmDto(
            id = this.id,
            name = this.name,
            enabled = this.enabled,
            timeRemaining = RemainingTime(
                hours = hoursRemaining.toInt(),
                minutes = minutesRemaining
            ),
            alarmTime = this.alarmTime,
        )
    }
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
    val timeRemaining: RemainingTime,
)

data class RemainingTime(val hours: Int, val minutes: Int)
data class AlarmTime(val alarmHour: String, val alarmMinute: String)