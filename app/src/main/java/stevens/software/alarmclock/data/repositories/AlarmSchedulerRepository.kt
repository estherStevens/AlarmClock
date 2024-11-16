package stevens.software.alarmclock.data.repositories

import stevens.software.alarmclock.data.AlarmItem
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class AlarmSchedulerRepository(val alarmScheduler: AlarmScheduler) {

    fun scheduleAlarm(alarmId: Int, alarmName: String, alarmTime: AlarmTime) {
        alarmScheduler.schedule(
            alarmId = alarmId,
            alarmName = alarmName,
            alarmTime = alarmTime
        )
    }

    fun cancelAlarm(alarmId: Int, alarmName: String, alarmTime: AlarmTime){
        alarmScheduler.cancel(
           alarmId = alarmId,
            alarmName = alarmName,
            alarmTime = alarmTime
        )
    }

}