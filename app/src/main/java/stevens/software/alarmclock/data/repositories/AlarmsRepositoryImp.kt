package stevens.software.alarmclock.data.repositories

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import stevens.software.alarmclock.data.Alarm
//import stevens.software.alarmclock.data.AlarmWithSelectedDays
import stevens.software.alarmclock.data.AlarmsDatabase
//import stevens.software.alarmclock.data.SelectedDays

class AlarmsRepositoryImp(val context: Context): AlarmsRepository {
    private val alarmDao = AlarmsDatabase.getDatabase(context).alarmDao()

    override suspend fun addAlarm(alarm: Alarm) : Result<Long> {
        val result = alarmDao.insertAlarm(alarm)
        return when {
            result == -1L ->  Result.failure(Throwable())
            else -> Result.success(result)
        }
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun updateAlarm(id: Int, enabled: Boolean) {
        alarmDao.updateAlarm(id, enabled)
    }

    override fun getAlarm(id: Int) : Flow<Alarm> {
        return alarmDao.getAlarm(id)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {

       val i=  alarmDao.getAllAlarms()
        return i
    }

//    override suspend fun addSelectedDaysForAlarm(selectedDays: SelectedDays) {
//        alarmDao.addSelectedDaysForAlarm(selectedDays)
//    }
//
//    override suspend fun updateSelectedDaysForAlarm(selectedDays: SelectedDays) {
//        alarmDao.updateSelectedDaysForAlarm(selectedDays)
//    }
//
//    override fun getAlarmWithSelectedDays(id: Int): Flow<AlarmWithSelectedDays> {
//       return  alarmDao.getAlarmWithSelectedDays(id)
//    }


}
