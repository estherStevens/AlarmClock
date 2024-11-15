package stevens.software.alarmclock.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AlarmSchedulerImp(val context: Context) : AlarmScheduler {

    val alarmManager: AlarmManager = context.getSystemService<AlarmManager>(AlarmManager::class.java)

    override fun schedule(alarm: AlarmItem) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra("ALARM_TRIGGERED", alarm.name)
        }
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
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