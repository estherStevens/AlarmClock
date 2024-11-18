package stevens.software.alarmclock.data

import java.time.DayOfWeek
import java.time.LocalDateTime

interface AlarmScheduler {
    fun schedule(alarmId: Int, alarmName: String, alarmTime: LocalDateTime, repeatingDays: MutableList<Int>)
    fun cancel(alarmId: Int, alarmName: String, alarmTime: AlarmTime)
}