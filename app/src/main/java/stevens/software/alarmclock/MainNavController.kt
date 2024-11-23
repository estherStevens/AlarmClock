package stevens.software.alarmclock

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import stevens.software.alarmclock.ui.triggeredAlarm.TriggeredAlarmScreen
import stevens.software.alarmclock.ui.alarms.AlarmsScreen

import stevens.software.alarmclock.ui.create_alarm.CreateAlarmScreen
import stevens.software.alarmclock.ui.create_alarm.CreateAlarmViewModel
import stevens.software.alarmclock.ui.triggeredAlarm.TriggeredAlarmViewModel


@Serializable
object Alarms

@Serializable
object CreateAlarm


@Serializable
data class AlarmTriggered(val alarmId: Int, val alarmName: String, val alarmTime: String)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Alarms) {
        composable<Alarms> {
            AlarmsScreen(
                onAddAlarmClicked = { navController.navigate(CreateAlarm) }
            )
        }
        composable<CreateAlarm> {
            CreateAlarmScreen(
                onCloseButtonClicked = { navController.popBackStack() },
                onSaveAlarmSuccess = { navController.navigate(Alarms) },
            )
        }

        composable<AlarmTriggered> (
            deepLinks = listOf(
                navDeepLink<AlarmTriggered>(basePath = "myapp://alarm_triggered")
            )
        ){
            val alarmId = it.toRoute<AlarmTriggered>().alarmId
            val alarmTime = it.toRoute<AlarmTriggered>().alarmTime
            val alarmName = it.toRoute<AlarmTriggered>().alarmName

            val viewModel : TriggeredAlarmViewModel = koinViewModel()
            viewModel.setDeepLinkData(alarmId, alarmName, alarmTime)

            TriggeredAlarmScreen(
                viewModel = viewModel,
                onTurnOffClicked = {
                    navController.navigate(Alarms)
                }
            )

        }
    }
}