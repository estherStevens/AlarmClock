package stevens.software.alarmclock.ui.create_alarm

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.AlarmItem
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmSchedulerImp
import stevens.software.alarmclock.data.AlarmTime
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import stevens.software.alarmclock.R
import java.time.LocalTime
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
            successSavingAlarm = false
        )
    )
    val uiState: StateFlow<CreateAlarmUiState> = _uiState

    fun updateAlarmHour(alarmHour: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    alarmHour = alarmHour,
                    saveButtonEnabled = true
                )
            }
        }
    }

    fun updateAlarmMinute(alarmMinute: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    alarmMinute = alarmMinute,
                    saveButtonEnabled = true
                )
            }
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
                        alarmTime = AlarmTime(
                            alarmHour = uiState.value.alarmHour,
                            alarmMinute = uiState.value.alarmMinute)
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


    fun CreateAlarmUiState.toAlarm() : Alarm {
        val name = if(this.alarmName.isEmpty()) "Alarm" else this.alarmName
        return Alarm(
            name = name,
            hour = this.alarmHour,
            minute = this.alarmMinute,
            enabled = true
        )
    }

}


data class CreateAlarmUiState(
    var alarmHour: String,
    val alarmMinute: String,
    val saveButtonEnabled: Boolean,
    val alarmName: String,
    val successSavingAlarm: Boolean,
    val errorSavingAlarm: Boolean
)

