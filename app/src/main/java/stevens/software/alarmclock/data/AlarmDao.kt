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

    @Query("Update alarms set enabled = :enabled where id = :id")
    suspend fun updateAlarm(id: Int, enabled: Boolean)

    @Query("Select * from alarms where id = :id")
    fun getAlarm(id: Int) : Flow<Alarm>

    @Query("Select * from alarms")
    fun getAllAlarms() : Flow<List<Alarm>>
}

//
//data class Alarm(
//    val id: Int,
//    val alarmName: String,
//    val alarmTime: LocalTime
//)
