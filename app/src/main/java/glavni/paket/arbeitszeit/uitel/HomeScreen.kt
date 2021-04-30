package glavni.paket.arbeitszeit.uitel

import android.content.Context
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
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
import androidx.lifecycle.LifecycleOwner
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.MainActivity
import glavni.paket.arbeitszeit.ui.theme.Green100
import glavni.paket.arbeitszeit.ui.theme.Green200
import glavni.paket.arbeitszeit.ui.theme.Red100
import glavni.paket.arbeitszeit.ui.theme.Red200
import glavni.paket.arbeitszeit.ui.viewwmodels.MainViewModel
import java.util.*

@Composable
fun HomeScreen(lifecycleOwner: LifecycleOwner, viewModel: MainViewModel){
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
            var enabled by remember { mutableStateOf(true) }
            var isVisible by remember { mutableStateOf(false) }
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
            var day: Day? = null
            viewModel.getLastDay().observe(lifecycleOwner, {
                if(it != null && it.timeLogOut == null) {
                    enabled = false
                    day = it
                }
                isVisible = true
            })
            if(isVisible) {
                val color = if(enabled) green else red
                val buttonColors = ButtonDefaults.buttonColors(
                    backgroundColor = color
                )
                val text = if(enabled) "LOG IN" else "LOG OUT"
                IconButton(
                    shape = RoundedCornerShape(30.dp),
                    onClick = {
                        val now = Calendar.getInstance().time
                        if(enabled) {
                            val newDay = Day(now, null)
                            viewModel.insertDay(newDay)
                        } else {
                            day?.timeLogOut = now
                            day?.let { viewModel.updateDay(it) }
                        }
                        Log.d("lista", "onClick $enabled")
                        enabled = !enabled
                    },
                    colors = buttonColors,
                    modifier = Modifier.fillMaxSize()
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
}