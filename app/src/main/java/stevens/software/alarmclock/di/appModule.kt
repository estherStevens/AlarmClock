package stevens.software.alarmclock.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import stevens.software.alarmclock.data.AlarmBroadcastReceiver
import stevens.software.alarmclock.data.AlarmScheduler
import stevens.software.alarmclock.data.AlarmSchedulerImp
import stevens.software.alarmclock.data.AlarmsDatabase
import stevens.software.alarmclock.data.repositories.AlarmSchedulerRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepositoryImp
import stevens.software.alarmclock.data.repositories.RingtoneRepository
import stevens.software.alarmclock.ui.alarms.AlarmsViewModel
import stevens.software.alarmclock.ui.create_alarm.CreateAlarmViewModel
import stevens.software.alarmclock.ui.triggeredAlarm.TriggeredAlarmViewModel

val appModule = module {
    viewModelOf(::AlarmsViewModel)
    viewModelOf(::CreateAlarmViewModel)
    viewModelOf(::TriggeredAlarmViewModel)
    singleOf(::AlarmsRepositoryImp) { bind<AlarmsRepository>() }
    singleOf(::AlarmSchedulerImp) { bind<AlarmScheduler>() }
    singleOf(::AlarmBroadcastReceiver)
    singleOf(::AlarmSchedulerRepository)
    singleOf(::RingtoneRepository)

    single {
        Room.databaseBuilder(
            androidApplication(),
            AlarmsDatabase::class.java,
            "alarms_database"
        ).build()
    }
    single { get<AlarmsDatabase>().alarmDao() }

}
