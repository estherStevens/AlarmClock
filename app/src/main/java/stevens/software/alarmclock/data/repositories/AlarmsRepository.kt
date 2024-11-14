package stevens.software.alarmclock.data.repositories

import kotlinx.coroutines.flow.Flow
import stevens.software.alarmclock.data.Alarm

interface AlarmsRepository {

    suspend fun addAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun updateAlarm(alarm: Alarm)

    fun getAllAlarms(): Flow<List<Alarm>>
}