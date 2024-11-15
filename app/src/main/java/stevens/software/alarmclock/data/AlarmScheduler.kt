package stevens.software.alarmclock.data

interface AlarmScheduler {
    fun schedule(alarmId: Int, alarmName: String, alarmTime: AlarmTime)
    fun cancel(alarm: AlarmItem)
}