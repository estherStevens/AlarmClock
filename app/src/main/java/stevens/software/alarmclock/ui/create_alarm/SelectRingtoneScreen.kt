package stevens.software.alarmclock.ui.create_alarm

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import stevens.software.alarmclock.R
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import stevens.software.alarmclock.data.repositories.RingtoneItem
import stevens.software.alarmclock.ui.alarms.montserratFontFamily


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectRingtoneScreen(
    viewModel: SelectRingtoneViewModel = koinViewModel(),
    onBackClicked: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

//    var isPermissionGranted by remember { mutableStateOf(false) }
//
//    val laucher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            if(isGranted){
//                isPermissionGranted = true
//            }
//        })

    val permissionState = rememberPermissionState(permission = android.Manifest.permission.WRITE_SETTINGS)

    LaunchedEffect(Unit) {
        if(!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

        SelectRingtone(
            ringtones = uiState.value.ringtones,
            selectedRingtone = RingtoneItem("", "", false),
            onRingtoneClicked = { ringtone ->
                viewModel.updateSelectedRingtone(ringtone)
            },
            onBackClicked = {
                viewModel.releaseMediaPlayback()
                onBackClicked()
            }
        )

}

@Composable
fun SelectRingtone(
    ringtones: List<RingtoneItem>,
    selectedRingtone: RingtoneItem,
    onRingtoneClicked: (RingtoneItem) -> Unit,
    onBackClicked: () -> Unit){
    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column {
            Icon(
                painter = painterResource(R.drawable.back),
                contentDescription = "",
                modifier = Modifier.clickable {
                    onBackClicked()
                },
                tint = Color.Unspecified
            )

            Spacer(Modifier.size(24.dp))
            RingtonesLazyColumn(
                ringtones = ringtones,
                onRingtoneClicked = onRingtoneClicked)

            }
        }
    }

@Composable
fun RingtonesLazyColumn(ringtones: List<RingtoneItem>,
                        onRingtoneClicked: (RingtoneItem) -> Unit) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(ringtones) { index, ringtone ->
            Box(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.white))
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    selectedIndex = if (selectedIndex == index) {
                        null
                    } else {
                        index
                    }
                    onRingtoneClicked(ringtone)
                }) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val ringtoneIcon = if(ringtone.title == "Silent") R.drawable.ringtone_silent else R.drawable.ringtone
                    Icon(painter = painterResource(ringtoneIcon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = ringtone.title,
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier.weight(2f)

                    )
                    if(selectedIndex == index) {
                        Icon(
                            painter = painterResource(R.drawable.tick),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SelectRingtoneScreenPreview() {
    MaterialTheme {
        SelectRingtone(
            onBackClicked = {},
            ringtones = listOf(
                RingtoneItem("Ringtone 1", "", true),
                RingtoneItem("Ringtone 2", "", false),
                RingtoneItem("Ringtone 3", "", false),
                RingtoneItem("Ringtone 4", "", false),
                RingtoneItem("Ringtone 5", "", false)
            ),
            onRingtoneClicked = {},
            selectedRingtone = RingtoneItem("Ringtone 1", "", false),
        )
    }
}