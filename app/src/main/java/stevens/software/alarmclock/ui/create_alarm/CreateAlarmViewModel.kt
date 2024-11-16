package stevens.software.alarmclock.ui.create_alarm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.ui.alarms.RemainingTime
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateAlarmViewModel(
    val alarmsRepository: AlarmsRepository,
    val alarmSchedulerRepository: AlarmSchedulerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateAlarmUiState(
            alarmHour = "",
            alarmMinute = "",
            saveButtonEnabled = false,
            alarmName = "",
            errorSavingAlarm = false,
            successSavingAlarm = false,
            timeRemaining = null
        )
    )
    val uiState: StateFlow<CreateAlarmUiState> = _uiState

    @RequiresApi(Build.VERSION_CODES.S)
    fun updateAlarmHour(alarmHour: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    alarmHour = alarmHour,
                    timeRemaining = getAlarmScheduledInTimeString(alarmHour, uiState.value.alarmMinute),
                    saveButtonEnabled = alarmHour.isNotEmpty() && uiState.value.alarmMinute.isNotEmpty()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun updateAlarmMinute(alarmMinute: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    alarmMinute = alarmMinute,
                    timeRemaining = getAlarmScheduledInTimeString(uiState.value.alarmHour, alarmMinute),
                    saveButtonEnabled = uiState.value.alarmHour.isNotEmpty() && alarmMinute.isNotEmpty()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getAlarmScheduledInTimeString(alarmHour: String, alarmMinute: String) : RemainingTime? {
        if(alarmHour.isNotEmpty() && alarmMinute.isNotEmpty() ) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarmHour.toInt())
                set(Calendar.MINUTE, alarmMinute.toInt())
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

            return RemainingTime(hoursRemaining.toInt(), minutesRemaining)
        } else {
            return null
        }
    }


    fun updateAlarmName(alarmName: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    alarmName = alarmName,
                )
            }
        }
    }

    fun saveAlarm() {
        viewModelScope.launch {
            val result = alarmsRepository.addAlarm(uiState.value.toAlarm())
            result.fold(
                onSuccess = { id ->
                    _uiState.update { it.copy(successSavingAlarm = true) }
                    alarmSchedulerRepository.scheduleAlarm(
                        alarmId = id.toInt(),
                        alarmName = uiState.value.alarmName,
                        alarmTime = convertToLocalDateTime(uiState.value.alarmHour, uiState.value.alarmMinute)
                    )
                },
                onFailure = {
                    _uiState.update {
                        it.copy(
                            errorSavingAlarm = true,
                        )
                    }
                }
            )
        }
    }

    fun convertToLocalDateTime(hour: String, minute: String): LocalDateTime{
        val formattedHour = String.format("%02d", hour.toInt())
        val formattedMinute = String.format("%02d", minute.toInt())

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(
            "$currentDate ${formattedHour}:${formattedMinute}",
            timeFormatter
        )
    }


    fun CreateAlarmUiState.toAlarm() : Alarm {
        val name = if(this.alarmName.isEmpty()) "Alarm" else this.alarmName
        return Alarm(
            name = name,
            enabled = true,
            alarmTime = convertToLocalDateTime(alarmHour, alarmMinute)
        )
    }

}


data class CreateAlarmUiState(
    var alarmHour: String,
    val alarmMinute: String,
    val saveButtonEnabled: Boolean,
    val alarmName: String,
    val successSavingAlarm: Boolean,
    val errorSavingAlarm: Boolean,
    val timeRemaining: RemainingTime?
)

