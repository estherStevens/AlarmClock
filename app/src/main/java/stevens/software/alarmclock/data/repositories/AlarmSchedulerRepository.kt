package stevens.software.alarmclock.data.repositories

import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.ui.alarms.AlarmTime
import java.time.LocalDateTime

class AlarmSchedulerRepository(val alarmScheduler: AlarmScheduler) {

    fun scheduleAlarm(alarmId: Int, alarmName: String, alarmTime: LocalDateTime) {
        alarmScheduler.schedule(
            alarmId = alarmId,
            alarmName = alarmName,
            alarmTime = alarmTime,
        )
    }

    fun cancelAlarm(alarmId: Int, alarmName: String, alarmTime: AlarmTime) {
        alarmScheduler.cancel(
            alarmId = alarmId,
            alarmName = alarmName,
            alarmTime = alarmTime
        )
    }
}