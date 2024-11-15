package stevens.software.alarmclock.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId
;
import java.util.Calendar

class AlarmSchedulerImp(val context: Context) : AlarmScheduler {

    val alarmManager: AlarmManager = context.getSystemService<AlarmManager>(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    override fun schedule(alarmId: Int, alarmName: String, alarmTime: AlarmTime) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra("TRIGGERED_ALARM_NAME", alarmName)
            putExtra("TRIGGERED_ALARM_TIME", "${alarmTime.alarmHour}:${alarmTime.alarmMinute}")
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmTime.alarmHour.toInt())
            set(Calendar.MINUTE, alarmTime.alarmMinute.toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                alarmId,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(alarm: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}