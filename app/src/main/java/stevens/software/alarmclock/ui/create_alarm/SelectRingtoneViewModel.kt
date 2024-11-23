package stevens.software.alarmclock.ui.create_alarm

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stevens.software.alarmclock.data.repositories.RingtoneItem
import stevens.software.alarmclock.data.repositories.RingtoneRepository
import stevens.software.alarmclock.ui.alarms.AlarmsUiState
import stevens.software.alarmclock.ui.alarms.DaysOfWeek
import stevens.software.alarmclock.ui.alarms.RemainingTime
import java.time.LocalTime

class SelectRingtoneViewModel(val ringtoneRepository: RingtoneRepository): ViewModel(){

    private val isLoading = MutableStateFlow<Boolean>(true)
    private val timeRemaining = MutableStateFlow<LocalTime?>(null)
    private val allRingtones = ringtoneRepository.allRingtones
    private val selectedRingtone = MutableStateFlow<RingtoneItem?>(null)

    val uiState = combine(
        allRingtones,
        isLoading,
    ) { ringtones, isLoading ->
        SelectRingtoneUiState(
            ringtones = ringtones,
            isLoading = false,
        )
    }.onStart {
        ringtoneRepository.getAllRingtones()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SelectRingtoneUiState(
            ringtones = emptyList(),
            isLoading = true
        )
    )

    fun updateSelectedRingtone(ringtone: RingtoneItem){
        viewModelScope.launch {
            ringtoneRepository.playPreviewOfAlarm(Uri.parse(ringtone.uri))
            selectedRingtone.emit(ringtone)
        }
    }

    fun releaseMediaPlayback(){
        ringtoneRepository.releaseMediaPLayer()
    }

}

data class SelectRingtoneUiState(
    val ringtones: List<RingtoneItem>,
//    val selectedRingtone: RingtoneItem,
    val isLoading: Boolean
)

