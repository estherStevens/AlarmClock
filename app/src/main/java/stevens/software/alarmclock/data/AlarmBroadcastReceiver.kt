package stevens.software.alarmclock.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import stevens.software.alarmclock.MainActivity

class AlarmBroadcastReceiver() : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val alarmName = p1?.getStringExtra("TRIGGERED_ALARM_NAME") ?: return
        val alarmTime = p1.getStringExtra("TRIGGERED_ALARM_TIME") ?: return

        if (p0 != null) {
            val activityIntent = Intent(p0, MainActivity::class.java).apply {
                data = "myapp://alarm_triggered/$alarmName/$alarmTime".toUri()
            }
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val pendingIntent = TaskStackBuilder.create(p0).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
//            p0.startActivity(activityIntent)
            pendingIntent?.send()

            println("triggered $alarmName")
        } else {
            return
        }
    }
}