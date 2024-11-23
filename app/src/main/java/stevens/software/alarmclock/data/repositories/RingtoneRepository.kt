package stevens.software.alarmclock.data.repositories

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
//import position
import kotlin.collections.mutableListOf

class RingtoneRepository(
    val context: Context,
    val ringtonesDataSource: RingtonesDataSource) {


//    var ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM))
    val allRingtones: MutableStateFlow<MutableList<RingtoneItem>> = MutableStateFlow(mutableListOf())
    var savedRingtone: Uri? = null

    var player: MediaPlayer? = null

    val title = MutableStateFlow("")

    suspend fun getRingtoneTitle(){
//        title.emit(ringtone.getTitle(context))
    }

    suspend fun getAllRingtones() {
        val list = mutableListOf<RingtoneItem>()

//        val i = RingtoneManager.getActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM)
        ringtonesDataSource.getAllRingtones().map { ringtones ->
            list.add(ringtones.toRingtoneItem())
        }
        allRingtones.emit(list)
    }


    fun playAlarmTone(){

//        println("plz" + savedRingtone)
        val i = RingtoneManager.getRingtone(context, savedRingtone)
        i.play()
    }

    fun stopAlarmTone(){
        val i = RingtoneManager.getRingtone(context, savedRingtone)
        println("122" + i.getTitle(context))
        println("122" + i.isPlaying)

        i.stop()
    }

    suspend fun playPreviewOfAlarm(uri: Uri){



        RingtoneManager.setActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM, uri)
        savedRingtone = RingtoneManager.getActualDefaultRingtoneUri(context,  RingtoneManager.TYPE_ALARM)


        title.emit(RingtoneManager.getRingtone(context, savedRingtone).getTitle(context))

        try {
            if(player != null && player?.isPlaying == true){
                player?.stop()
            }
            player = MediaPlayer.create(context, savedRingtone)
            player?.start()

        } catch (e: IllegalStateException) {
            println("Error setting data source")
        }


    }

    fun releaseMediaPLayer(){
        player?.stop()
        player?.release()
        player = null
    }

    fun stevens.software.alarmclock.data.repositories.Ringtone.toRingtoneItem() : RingtoneItem {
        return RingtoneItem(
            title = this.title,
            uri = this.uri,
            selected = isRingtoneSelected(this)
        )
    }

    fun isRingtoneSelected(ringtone: stevens.software.alarmclock.data.repositories.Ringtone): Boolean{
        return ringtone.title == ringtonesDataSource.getDefaultRingtoneTitle()
    }

}
data class RingtoneItem(val title: String, val uri: String, var selected: Boolean)
