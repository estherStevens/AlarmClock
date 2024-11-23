package stevens.software.alarmclock.ui.create_alarm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import stevens.software.alarmclock.data.Alarm
//import stevens.software.alarmclock.data.SelectedDays
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.data.repositories.RingtoneRepository
import stevens.software.alarmclock.ui.alarms.AlarmsUiState
import stevens.software.alarmclock.ui.alarms.Days
import stevens.software.alarmclock.ui.alarms.DaysOfWeek
//import stevens.software.alarmclock.ui.alarms.DaysOfWeek
import stevens.software.alarmclock.ui.alarms.RemainingTime
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.map

class CreateAlarmViewModel(
    val alarmsRepository: AlarmsRepository,
    val alarmSchedulerRepository: AlarmSchedulerRepository,
    val ringtoneRepository: RingtoneRepository,
) : ViewModel() {

    private val alarmHour = MutableStateFlow<String>("")
    private val alarmMinute = MutableStateFlow<String>("")
    private val saveButtonEnabled = MutableStateFlow<Boolean>(false)
    private val alarmName = MutableStateFlow<String>("Alarm")
    private val successSavingAlarm = MutableStateFlow<Boolean>(false)
    private val timeRemaining = MutableStateFlow<RemainingTime?>(null)

    val uiState = combine(
        alarmHour,
        alarmMinute,
        saveButtonEnabled,
        alarmName,
        successSavingAlarm,
        timeRemaining,
        ) { alarmHour, alarmMinute, saveButtonEnabled, alarmName, successSavingAlarm, timeRemaining, ->
        CreateAlarmUiState(
            alarmHour = alarmHour,
            alarmMinute = alarmMinute,
            saveButtonEnabled = saveButtonEnabled,
            alarmName = alarmName,
            successSavingAlarm = successSavingAlarm,
            timeRemaining = timeRemaining,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        CreateAlarmUiState(
            alarmHour = "",
            alarmMinute = "",
            saveButtonEnabled = false,
            alarmName = "Alarm",
            successSavingAlarm = false,
            timeRemaining = null,
        )
    )

//    private val _uiState = MutableStateFlow(
//        CreateAlarmUiState(
//            alarmHour = "",
//            alarmMinute = "",
//            saveButtonEnabled = false,
//            alarmName = "Alarm",
//            errorSavingAlarm = false,
//            successSavingAlarm = false,
//            timeRemaining = null,
//            repeatingDays = initialDaysOfTheWeek(),
//            selectedRingtone = ringtoneRepository.getRingtoneTitle()
//        )
//    )

//    val i = ringtoneTitle.map { title ->
//        CreateAlarmUiState(
//            alarmHour = "",
//            alarmMinute = "",
//            saveButtonEnabled = false,
//            alarmName = "Alarm",
//            errorSavingAlarm = false,
//            successSavingAlarm = false,
//            timeRemaining = null,
//            repeatingDays = initialDaysOfTheWeek(),
//            selectedRingtone = title
//        )
//    }
//

//    val uiState: StateFlow<CreateAlarmUiState> = _uiState


    @RequiresApi(Build.VERSION_CODES.S)
    fun updateAlarmHour(hour: String) {
        viewModelScope.launch {
            alarmHour.emit(hour)
            timeRemaining.emit(getAlarmScheduledInTimeString(hour, uiState.value.alarmMinute))
            saveButtonEnabled.emit(uiState.value.alarmHour.isNotEmpty() && uiState.value.alarmMinute.isNotEmpty())

//            _uiState.update {
//                it.copy(
//                    alarmHour = alarmHour,
//                    timeRemaining = getAlarmScheduledInTimeString(alarmHour, uiState.value.alarmMinute),
//                    saveButtonEnabled = alarmHour.isNotEmpty() && uiState.value.alarmMinute.isNotEmpty()
//                )
//            }
        }
    }


    fun initialDaysOfTheWeek(): MutableList<DaysOfWeek>{
        return mutableListOf<DaysOfWeek>(
            DaysOfWeek(day = Calendar.MONDAY, selected = false),
            DaysOfWeek(day = Calendar.TUESDAY, selected = false),
            DaysOfWeek(day = Calendar.WEDNESDAY, selected = false),
            DaysOfWeek(day = Calendar.THURSDAY, selected = false),
            DaysOfWeek(day = Calendar.FRIDAY, selected = false),
            DaysOfWeek(day = Calendar.SATURDAY, selected = false),
            DaysOfWeek(day = Calendar.SUNDAY, selected = false),
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun updateAlarmMinute(minute: String) {
        viewModelScope.launch {
            alarmMinute.emit(minute)
            timeRemaining.emit(getAlarmScheduledInTimeString(uiState.value.alarmHour, minute))
            saveButtonEnabled.emit(uiState.value.alarmHour.isNotEmpty() && uiState.value.alarmMinute.isNotEmpty())

//            _uiState.update {
//                it.copy(
//                    alarmMinute = alarmMinute,
//                    timeRemaining = getAlarmScheduledInTimeString(uiState.value.alarmHour, alarmMinute),
//                    saveButtonEnabled = uiState.value.alarmHour.isNotEmpty() && alarmMinute.isNotEmpty()
//                )
//            }
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


    fun updateAlarmName(name: String) {
        viewModelScope.launch {
            alarmName.emit(name)
//            _uiState.update {
//                it.copy(
//                    alarmName = alarmName,
//                )
//            }
        }
    }

    fun saveAlarm() {
        viewModelScope.launch {
            val result = alarmsRepository.addAlarm(uiState.value.toAlarm())
            result.fold(
                onSuccess = { id ->
//                    alarmsRepository.addSelectedDaysForAlarm(uiState.value.toSelectedDays(id.toInt()))
//                    _uiState.update { it.copy(successSavingAlarm = true) }
                    successSavingAlarm.emit(true)
                    alarmSchedulerRepository.scheduleAlarm(
                        alarmId = id.toInt(),
                        alarmName = uiState.value.alarmName,
                        alarmTime = convertToLocalDateTime(uiState.value.alarmHour, uiState.value.alarmMinute),
                    )
                },
                onFailure = {
//                    _uiState.update {
//                        it.copy(
//                            errorSavingAlarm = true,
//                        )
//                    }
                }
            )
        }
    }

    fun getRepeatingDays(repeatingDaysList: MutableList<DaysOfWeek>) :  MutableList<Int>{
        val repeatingDays : MutableList<Int> = mutableListOf()
        repeatingDaysList.map {
            if (it.selected  == true) repeatingDays.add(it.day)
        }
        return repeatingDays
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


//    fun initialDaysOfTheWeek(): List<DaysOfWeek>{
//        return listOf<DaysOfWeek>(
//            DaysOfWeek(day = DayOfWeek.MONDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.TUESDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.WEDNESDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.THURSDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.FRIDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.SATURDAY, selected = false),
//            DaysOfWeek(day = DayOfWeek.SUNDAY, selected = false),
//        )
//    }
//


    fun CreateAlarmUiState.toAlarm() : Alarm {
        val name = if(this.alarmName.isEmpty()) "Alarm" else this.alarmName
        return Alarm(
            name = name,
            enabled = true,
            alarmTime = convertToLocalDateTime(alarmHour, alarmMinute),
        )
    }

    /*fun CreateAlarmUiState.toSelectedDays(alarmId: Int) : SelectedDays {
      return SelectedDays(
            alarmId = alarmId,
            repeatOnMondays = this.testDays.find { it.day == DayOfWeek.MONDAY }?.selected == true,
            repeatOnTuesdays = this.testDays.find { it.day == DayOfWeek.TUESDAY }?.selected == true,
            repeatOnWednesdays = this.testDays.find { it.day == DayOfWeek.WEDNESDAY }?.selected == true,
            repeatOnThursdays = this.testDays.find { it.day == DayOfWeek.THURSDAY }?.selected == true,
            repeatOnFridays = this.testDays.find { it.day == DayOfWeek.FRIDAY }?.selected == true,
            repeatOnSaturdays = this.testDays.find { it.day == DayOfWeek.SATURDAY }?.selected == true,
            repeatOnSundays = this.testDays.find { it.day == DayOfWeek.SUNDAY }?.selected == true,
        )
    }*/


}




data class CreateAlarmUiState(
    var alarmHour: String,
    val alarmMinute: String,
    val saveButtonEnabled: Boolean,
    val alarmName: String,
    val successSavingAlarm: Boolean,
    val timeRemaining: RemainingTime?,
)

fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple),
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}