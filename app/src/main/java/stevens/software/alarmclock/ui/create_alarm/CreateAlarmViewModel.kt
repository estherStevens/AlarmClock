package stevens.software.alarmclock.ui.create_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val i =  LocalDateTime.parse(
            "${currentDate} ${hour}:${minute}",
            timeFormatter
        )
        return i
    }


    fun CreateAlarmUiState.toAlarm() : Alarm {
        val name = if(this.alarmName.isEmpty()) "Alarm" else this.alarmName
        return Alarm(
            name = name,
            enabled = true,
            alarmTime = convertToLocalDateTime(this.alarmHour, this.alarmMinute)
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

