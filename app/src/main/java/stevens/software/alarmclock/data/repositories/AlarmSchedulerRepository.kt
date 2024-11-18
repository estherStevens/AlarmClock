package stevens.software.alarmclock.data.repositories

import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmTime
import stevens.software.alarmclock.ui.alarms.DaysOfWeek
import java.time.DayOfWeek
import java.time.LocalDateTime

class AlarmSchedulerRepository(val alarmScheduler: AlarmScheduler) {

    fun scheduleAlarm(alarmId: Int, alarmName: String, alarmTime: LocalDateTime, repeatingDays: MutableList<Int>) {
        alarmScheduler.schedule(
            alarmId = alarmId,
            alarmName = alarmName,
            alarmTime = alarmTime,
            repeatingDays = repeatingDays
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