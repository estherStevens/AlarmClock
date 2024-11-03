package stevens.software.alarmclock

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Alarms

@Composable
fun MainNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Alarms) {
        composable<Alarms> {

        }
    }
}