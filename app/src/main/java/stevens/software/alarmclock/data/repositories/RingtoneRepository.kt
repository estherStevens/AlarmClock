package stevens.software.alarmclock.data.repositories

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager

class RingtoneRepository(val context: Context) {

    var ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM))

    fun playAlarmTone(){
        ringtone.play()
    }

    fun stopAlarmTone(){
        ringtone.stop()
    }

}