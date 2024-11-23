package stevens.software.alarmclock.data

import stevens.software.alarmclock.ui.alarms.AlarmTime
import java.time.LocalDateTime

interface AlarmScheduler {
    fun schedule(alarmId: Int, alarmName: String, alarmTime: LocalDateTime)
    fun cancel(alarmId: Int, alarmName: String, alarmTime: AlarmTime)
}