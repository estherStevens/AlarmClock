package stevens.software.alarmclock.data

interface AlarmScheduler {
    fun schedule(alarm: AlarmItem)
    fun cancel(alarm: AlarmItem)
}