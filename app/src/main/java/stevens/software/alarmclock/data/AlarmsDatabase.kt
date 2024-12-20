package stevens.software.alarmclock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [Alarm::class], version = 8)
abstract class AlarmsDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {

        @Volatile
        private var Instance: AlarmsDatabase? = null

        fun getDatabase(context: Context): AlarmsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AlarmsDatabase::class.java, "alarms_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}