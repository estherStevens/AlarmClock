package stevens.software.alarmclock.data.repositories

import android.content.Context
import android.media.RingtoneManager
import kotlinx.coroutines.flow.MutableStateFlow

class RingtonesDataSource(val context: Context) {
//    val allRingtones: MutableStateFlow<List<Ringtone>> = MutableStateFlow(listOf())

    fun getDefaultRingtoneTitle() : String {
        val ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM)
        val defaultRingtone = RingtoneManager.getRingtone(context, ringtoneUri)
//
        return defaultRingtone.getTitle(context)
    }

    fun getAllRingtones(): List<Ringtone> {
        val ringtones = mutableListOf<Ringtone>()

        val manager = RingtoneManager(context)
        manager.setType(RingtoneManager.TYPE_ALARM)

        val cursor = manager.cursor

        while(cursor.moveToNext()){
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            val baseUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)

            ringtones.add(Ringtone(title = title, uri = "$baseUri/$id?title=$title"))
        }

        return ringtones
    }
}

data class Ringtone(val title: String, val uri: String)