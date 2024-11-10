package stevens.software.alarmclock

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import stevens.software.alarmclock.ui.alarms.AlarmsScreen
import stevens.software.alarmclock.ui.create_alarm.CreateAlarmScreen


@Serializable
object Alarms

@Serializable
object CreateAlarm

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
                onCloseButtonClicked = { navController.popBackStack() }
            )
        }
    }
}