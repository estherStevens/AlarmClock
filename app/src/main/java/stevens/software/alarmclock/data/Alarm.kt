package stevens.software.alarmclock.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalTime

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val hour: String,
    val minute: String,
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(time: LocalTime?): String? {
        return time?.toString()
    }
}