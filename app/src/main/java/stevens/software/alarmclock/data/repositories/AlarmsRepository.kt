package stevens.software.alarmclock.data.repositories

import kotlinx.coroutines.flow.Flow
import stevens.software.alarmclock.data.Alarm

interface AlarmsRepository {

    suspend fun addAlarm(alarm: Alarm): Result<Long>

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun updateAlarm(id: Int, enabled: Boolean)

    fun getAlarm(id: Int) : Flow<Alarm>

    fun getAllAlarms(): Flow<List<Alarm>>
}