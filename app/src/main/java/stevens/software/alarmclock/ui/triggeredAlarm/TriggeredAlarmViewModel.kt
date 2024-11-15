package stevens.software.alarmclock.ui.triggeredAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stevens.software.alarmclock.data.AlarmItem
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class TriggeredAlarmViewModel(val alarmScheduler: AlarmScheduler) : ViewModel() {

    private val _uiState = MutableStateFlow<TriggeredAlarmUiState>(TriggeredAlarmUiState(
        alarmTime = AlarmTime(alarmHour = "", alarmMinute = ""),
        alarmName = ""
    ))

    val uiState = _uiState.asStateFlow()

    fun setDeepLinkData(alarmName: String, alarmTime: String){
        val alarmSegments = alarmTime.split(":")

        viewModelScope.launch{
            _uiState.update {
                TriggeredAlarmUiState(
                    alarmName = alarmName,
                    alarmTime = AlarmTime(
                        alarmHour = alarmSegments[0],
                        alarmMinute = alarmSegments[1]
                    )
                )
            }
        }

    }

    /*fun turnOffAlarm(){
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeAlarmScheduledFor = LocalDateTime.parse(
            "${currentDate}T${uiState.value.alarmTime.alarmHour}:${uiState.value.alarmTime.alarmMinute}"
        )
        alarmScheduler.cancel(
            AlarmItem(
                name = uiState.value.alarmName,
                time = timeAlarmScheduledFor
            )
        )
    }*/

}

data class TriggeredAlarmUiState(val alarmName: String, val alarmTime: AlarmTime)