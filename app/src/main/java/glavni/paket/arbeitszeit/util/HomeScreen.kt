package glavni.paket.arbeitszeit.util

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import androidx.compose.runtime.livedata.observeAsState
import glavni.paket.arbeitszeit.BaseApplication
import glavni.paket.arbeitszeit.ui.theme.*
import java.util.*
import javax.inject.Inject

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
            val day = viewModel.getLastDay.observeAsState().value
            val icon = if(enabled) Icons.Default.PlayCircle else Icons.Default.PauseCircle
            IconButton(
                onClick = {
                   clicked = true
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if(clicked) {
                    val now = Calendar.getInstance()
                    now.set(Calendar.SECOND, 0)
                    now.set(Calendar.MILLISECOND, 0)
                    if(enabled) {
                        val newDay = Day(now.time, null)
                        viewModel.insertDay(newDay)
                        viewModel.myPreference.setLogIn(false)
                        viewModel.myPreference.setLastLogIn(now.time.time)
                        clicked = false
                    } else {
                        if(day != null && day.timeLogIn != null) {
                            if(day.timeLogIn!!.equals(now.time)) {
                                viewModel.deleteDay(day)
                                val lastDay = viewModel.getLastDay.observeAsState().value
                                if(lastDay != null) {
                                    viewModel.myPreference.setLogIn(true)
                                    lastDay.timeLogIn?.let { viewModel.myPreference.setLastLogIn(it.time) }
                                    clicked = false
                                }
                            } else {
                                day.timeLogOut = now.time
                                day.let { viewModel.updateDay(it) }
                                viewModel.myPreference.setLogIn(true)
                                clicked = false
                            }
                        }
                    }
                    enabled = !enabled
                }
                Icon(
                    icon,
                    contentDescription = "Log in",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.
                            fillMaxSize()
                )
            }
        }
    }
}