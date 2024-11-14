package stevens.software.alarmclock

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import stevens.software.alarmclock.MainActivity
import stevens.software.alarmclock.di.appModule

//import stevens.software.alarmclock.data.AppContainerImp

class AlarmClockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AlarmClockApplication)
            modules(appModule)
        }
    }

}