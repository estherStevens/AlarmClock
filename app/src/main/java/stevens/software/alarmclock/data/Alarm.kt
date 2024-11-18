package stevens.software.alarmclock.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import java.time.LocalDateTime

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val alarmTime: LocalDateTime,
    val enabled: Boolean,
    val repeatOnMondays: Boolean,
    val repeatOnTuesdays: Boolean,
    val repeatOnWednesdays: Boolean,
    val repeatOnThursdays: Boolean,
    val repeatOnFridays: Boolean,
    val repeatOnSaturdays: Boolean,
    val repeatOnSundays: Boolean,
)

/*@Entity(tableName = "selected_days")
data class SelectedDays(
    @PrimaryKey(autoGenerate = true)
    val daysId: Int = 0,
    val alarmId: Int,
    val repeatOnMondays: Boolean,
    val repeatOnTuesdays: Boolean,
    val repeatOnWednesdays: Boolean,
    val repeatOnThursdays: Boolean,
    val repeatOnFridays: Boolean,
    val repeatOnSaturdays: Boolean,
    val repeatOnSundays: Boolean,
)*/

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(time: LocalDateTime?): String? {
        return time?.toString()
    }
}

/*data class AlarmWithSelectedDays(
    @Embedded val alarm: Alarm,
    @Relation(
        parentColumn = "id",
        entityColumn = "alarmId"
    )
    val selectedDays: SelectedDays
)*/