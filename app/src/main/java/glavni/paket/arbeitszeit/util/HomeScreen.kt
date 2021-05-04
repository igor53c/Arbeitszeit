package glavni.paket.arbeitszeit.util

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import glavni.paket.arbeitszeit.ui.theme.*
import java.util.*

@Composable
fun HomeScreen(viewModel: MainViewModel = hiltNavGraphViewModel()){
    val constraints = ConstraintSet {
        val greenBox = createRefFor("greenBox")

        constrain(greenBox) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            width = Dimension.value(150.dp)
            height = Dimension.value(150.dp)
        }
    }
    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .layoutId("greenBox")
        ) {
            var enabled by remember { mutableStateOf(viewModel.myPreference.getLogIn())}
            val day = viewModel.getLastDay.observeAsState().value
            val infiniteTransition = rememberInfiniteTransition()
            val red by infiniteTransition.animateColor(
                initialValue = Red100,
                targetValue = Red200,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
            val green by infiniteTransition.animateColor(
                initialValue = Green100,
                targetValue = Green200,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
            val color = if(enabled) green else red
            val borderColor = if(enabled) Color.Green else Color.Red
            val buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = color
            )
            val text = if(enabled) "LOG IN" else "LOG OUT"
            Button(
                shape = RoundedCornerShape(30.dp),
                onClick = {
                    val now = Calendar.getInstance()
                    now.set(Calendar.SECOND, 0)
                    now.set(Calendar.MILLISECOND, 0)
                    if(enabled) {
                        val newDay = Day(now.time, null)
                        viewModel.insertDay(newDay)
                        viewModel.myPreference.setLogIn(false)
                    } else {
                        day?.timeLogOut = now.time
                        day?.let { viewModel.updateDay(it) }
                        viewModel.myPreference.setLogIn(true)
                    }
                    enabled = !enabled
                },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxSize(),
                border = BorderStroke(
                    width = 10.dp,
                    color = borderColor
                )
            ) {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }

    }
}