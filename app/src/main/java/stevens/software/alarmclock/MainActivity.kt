package stevens.software.alarmclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import stevens.software.alarmclock.di.appModule
import stevens.software.alarmclock.ui.theme.AlarmClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmClockTheme {
                startKoin {
                    androidContext(this@MainActivity)
                    modules(appModule)
                }
                enableEdgeToEdge()


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavController()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmClockTheme {
        Greeting("Android")
    }
}