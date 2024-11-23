package stevens.software.alarmclock.ui.triggeredAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stevens.software.alarmclock.ui.alarms.AlarmTime
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.data.repositories.RingtoneRepository

class TriggeredAlarmViewModel(
    val alarmScheduler: AlarmScheduler,
    val alarmsRepository: AlarmsRepository,
    val ringtoneRepository: RingtoneRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TriggeredAlarmUiState>(
        TriggeredAlarmUiState(
            alarmId = -1,
            alarmTime = AlarmTime(alarmHour = "", alarmMinute = ""),
            alarmName = "",
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        ringtoneRepository.playAlarmTone()
    }

    fun setDeepLinkData(alarmId: Int, alarmName: String, alarmTime: String) {
        val alarmSegments = alarmTime.split(":")

        viewModelScope.launch {
            _uiState.update {
                TriggeredAlarmUiState(
                    alarmId = alarmId,
                    alarmName = alarmName,
                    alarmTime = AlarmTime(
                        alarmHour = alarmSegments[0],
                        alarmMinute = alarmSegments[1]
                    )
                )
            }
        }
    }

    fun updateEnabledStateOfAlarm() {
        viewModelScope.launch {
            alarmsRepository.updateAlarm(uiState.value.alarmId, false)
        }
    }

    fun turnOffAlarm() {
        ringtoneRepository.stopAlarmTone()
        updateEnabledStateOfAlarm()
        alarmScheduler.cancel(
            alarmId = uiState.value.alarmId,
            alarmName = uiState.value.alarmName,
            alarmTime = uiState.value.alarmTime
        )
    }
}

data class TriggeredAlarmUiState(
    val alarmId: Int,
    val alarmName: String,
    val alarmTime: AlarmTime
)