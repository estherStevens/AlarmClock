package stevens.software.alarmclock.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import stevens.software.alarmclock.data.AlarmsDatabase
import stevens.software.alarmclock.data.repositories.AlarmsRepository
import stevens.software.alarmclock.data.repositories.AlarmsRepositoryImp
//import stevens.software.alarmclock.data.repositories.AlarmsRepository
//import stevens.software.alarmclock.data.repositories.AlarmsRepository
//import stevens.software.alarmclock.data.repositories.AlarmsRepositoryImp
import stevens.software.alarmclock.ui.alarms.AlarmsViewModel
import stevens.software.alarmclock.ui.create_alarm.CreateAlarmViewModel
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.EmptyCoroutineContext.get

val appModule = module {
    viewModel { AlarmsViewModel(get<AlarmsRepository>()) }
    viewModel { CreateAlarmViewModel() }
    singleOf(::AlarmsRepositoryImp) { bind<AlarmsRepository>() }
    single {
        Room.databaseBuilder(
            androidApplication(),
            AlarmsDatabase::class.java,
            "alarms_database"
        ).build()
    }
    single { get<AlarmsDatabase>().alarmDao() }

}
