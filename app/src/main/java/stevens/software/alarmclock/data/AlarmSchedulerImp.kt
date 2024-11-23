package stevens.software.alarmclock.data

//import AlarmReceiver
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
//import now
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
;
import java.util.Calendar

class AlarmSchedulerImp(val context: Context) : AlarmScheduler {

    val alarmManager: AlarmManager = context.getSystemService<AlarmManager>(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    override fun schedule(alarmId: Int, alarmName: String, alarmTime: LocalDateTime) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra("TRIGGERED_ALARM_NAME", alarmName)
            putExtra("TRIGGERED_ALARM_TIME", "${alarmTime.hour}:${alarmTime.minute}")
            putExtra("TRIGGERED_ALARM_ID", alarmId)
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmTime.hour.toInt())
            set(Calendar.MINUTE, alarmTime.minute.toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        setOneOffAlarm(
            calendar = calendar,
            alarmId = alarmId,
            alarmIntent = alarmIntent
        )

    }

    override fun cancel(alarmId: Int, alarmName: String, alarmTime: AlarmTime) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @SuppressLint("MissingPermission")
    fun setOneOffAlarm(calendar: Calendar, alarmId: Int, alarmIntent: Intent){

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, null),
            PendingIntent.getBroadcast(
                context,
                alarmId,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}
