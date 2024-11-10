package stevens.software.alarmclock.ui.create_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class CreateAlarmViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(CreateAlarmUiState(
        alarmHour = "",
        alarmMinute = "",
        saveButtonEnabled = false,
        alarmName = ""

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

}



data class CreateAlarmUiState(
    var alarmHour: String,
    val alarmMinute: String,
    val saveButtonEnabled: Boolean,
    val alarmName: String
)