package stevens.software.alarmclock.data.repositories

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import stevens.software.alarmclock.data.Alarm
import stevens.software.alarmclock.data.AlarmDao
import stevens.software.alarmclock.data.AlarmsDatabase

class AlarmsRepositoryImp(val context: Context): AlarmsRepository {
    private val alarmDao = AlarmsDatabase.getDatabase(context).alarmDao()

    override suspend fun addAlarm(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
       val i=  alarmDao.getAllAlarms()
        return i
    }
}