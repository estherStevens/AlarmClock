package stevens.software.alarmclock.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver(): BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val alarmName = p1?.getStringExtra("ALARM_TRIGGERED") ?: return
        println("triggered $alarmName")
    }

}