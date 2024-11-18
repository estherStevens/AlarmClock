package stevens.software.alarmclock.data.repositories

import kotlinx.coroutines.flow.Flow
import stevens.software.alarmclock.data.Alarm
//import stevens.software.alarmclock.data.AlarmWithSelectedDays
//import stevens.software.alarmclock.data.SelectedDays

interface AlarmsRepository {

    suspend fun addAlarm(alarm: Alarm): Result<Long>

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun updateAlarm(id: Int, enabled: Boolean)

    fun getAlarm(id: Int) : Flow<Alarm>

    fun getAllAlarms(): Flow<List<Alarm>>

//    suspend fun addSelectedDaysForAlarm(selectedDays: SelectedDays)
//
//    suspend fun updateSelectedDaysForAlarm(selectedDays: SelectedDays)
//
//    fun getAlarmWithSelectedDays(id: Int): Flow<AlarmWithSelectedDays>
}