package glavni.paket.arbeitszeit.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.util.*

@ObsoleteCoroutinesApi
@Composable
fun HomeScreen(viewModel: MainViewModel = hiltNavGraphViewModel()){
    val constraints = ConstraintSet {
        val greenBox = createRefFor("greenBox")

        constrain(greenBox) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            width = Dimension.value(200.dp)
            height = Dimension.value(200.dp)
        }
    }
    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .layoutId("greenBox")
        ) {
            var enabled by remember { mutableStateOf(viewModel.myPreference.getLogIn())}
            var clicked by remember { mutableStateOf(false)}
            val period = viewModel.getLastPeriodLive.observeAsState().value
            val icon = if(enabled) Icons.Default.PlayCircle else Icons.Default.PauseCircle
            IconButton(
                onClick = {
                   clicked = true
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if(clicked) {
                    val now = Calendar.getInstance(Locale.GERMANY)
                    now.set(Calendar.SECOND, 0)
                    now.set(Calendar.MILLISECOND, 0)
                    if(enabled) {
                        val newPeriod = Period(now.time, null,true,null)
                        viewModel.myPreference.setLogIn(false)
                        viewModel.myPreference.setLastLogIn(now.time.time)
                        clicked = insertInDb(newPeriod)
                        if(!clicked) enabled = !enabled
                    } else {
                        if(period?.timeLogIn != null) {
                            period.timeLogOut = now.time
                            val timeLogIn = period.timeLogIn!!.time
                            period.workingTime =  now.time.time - timeLogIn
                            if(period.workingTime!! < 1) {
                                if(!deleteData(period)) {
                                    clicked = false
                                    enabled = !enabled
                                }
                            } else {
                                if(!updateInDb(period, null, null)) {
                                    viewModel.myPreference.setLogIn(true)
                                    clicked = false
                                    enabled = !enabled
                                }
                            }
                        }
                    }
                }
                Icon(
                    icon,
                    contentDescription = "Log in",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

