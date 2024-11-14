package stevens.software.alarmclock.ui.create_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.repositories.AlarmsRepository

class CreateAlarmViewModel(val alarmsRepository: AlarmsRepository): ViewModel() {

    private val _uiState = MutableStateFlow(CreateAlarmUiState(
        alarmHour = "",
        alarmMinute = "",
        saveButtonEnabled = false,
        alarmName = "",
        errorSavingAlarm = false,
        successSavingAlarm = false
    ))
    val uiState: StateFlow<CreateAlarmUiState> = _uiState

    fun updateAlarmHour(alarmHour: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(
                    alarmHour = alarmHour,
                    saveButtonEnabled = true
                )
            }
        }
    }

    fun updateAlarmMinute(alarmMinute: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(
                    alarmMinute = alarmMinute,
                    saveButtonEnabled = true
                )
            }
        }
    }


    fun updateAlarmName(alarmName: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(
                    alarmName = alarmName,
                )
            }
        }
    }

    fun saveAlarm(){
        viewModelScope.launch{
            val result = alarmsRepository.addAlarm(uiState.value.toAlarm())
            if(result == Result.success(Unit)) {
                _uiState.update {
                    it.copy(
                        successSavingAlarm = true,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorSavingAlarm = true,
                    )
                }
            }
        }
    }


    fun CreateAlarmUiState.toAlarm() =
         Alarm(
            name = this.alarmName,
            hour = this.alarmHour,
            minute = this.alarmMinute
        )


}



data class CreateAlarmUiState(
    var alarmHour: String,
    val alarmMinute: String,
    val saveButtonEnabled: Boolean,
    val alarmName: String,
    val successSavingAlarm: Boolean,
    val errorSavingAlarm: Boolean
)