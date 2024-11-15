package stevens.software.alarmclock.data

import java.time.LocalDateTime
import java.time.LocalTime

data class AlarmItem(val name: String, val time: LocalDateTime)

data class AlarmTime(val alarmHour: String, val alarmMinute: String)