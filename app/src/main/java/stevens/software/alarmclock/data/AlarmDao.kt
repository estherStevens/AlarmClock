package stevens.software.alarmclock.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insertAlarm(alarm: Alarm) : Long

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Query("Select * from alarms")
    fun getAllAlarms() : Flow<List<Alarm>>
}

//
//data class Alarm(
//    val id: Int,
//    val alarmName: String,
//    val alarmTime: LocalTime
//)
