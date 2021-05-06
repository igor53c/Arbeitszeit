package glavni.paket.arbeitszeit.util.hoursscreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glavni.paket.arbeitszeit.db.Day
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun AnimatedListWeek(day: Day?) {
    val animatedProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 360f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colors.onSurface)
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer(rotationX = animatedProgress.value)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        var logIn = ""
        var logOut = ""
        var logInDay = "--"
        var logInName = "---"
        var diff = ""
        if(day != null) {
            if(day.timeLogIn != null) {
                logIn = SimpleDateFormat("HH:mm").format(day.timeLogIn!!)
                logInDay = SimpleDateFormat("dd").format(day.timeLogIn!!)
                logInName = SimpleDateFormat("E").format(day.timeLogIn!!)
            }
            if(day.timeLogOut != null) {
                logOut = SimpleDateFormat("HH:mm").format(day.timeLogOut!!)
                val timeDifference = day.timeLogOut!!.time - day.timeLogIn!!.time
                val minute = timeDifference / (1000 * 60) % 60
                val hour = timeDifference / (1000 * 60 * 60) % 24
                diff = String.format(Locale.getDefault(), "%02d:%02d", Math.abs(hour), Math.abs(minute));
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = logInDay,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .width(50.dp),
            text = logInName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = logIn,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = logOut,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = diff,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun animatedListDay(day: Day?, state: Boolean): Boolean {
    var showDay by remember { mutableStateOf(state) }
    val animatedProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 360f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colors.onSurface)
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer(rotationX = animatedProgress.value)
            .clickable {
                showDay = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        var logIn = ""
        var logOut = ""
        var logInDay = "--"
        var logInName = "---"
        var diff = ""
        if(day != null) {
            if(day.timeLogIn != null) {
                logIn = SimpleDateFormat("HH:mm").format(day.timeLogIn!!)
                logInDay = SimpleDateFormat("dd").format(day.timeLogIn!!)
                logInName = SimpleDateFormat("E").format(day.timeLogIn!!)
            }
            if(day.timeLogOut != null) {
                logOut = SimpleDateFormat("HH:mm").format(day.timeLogOut!!)
                val timeDifference = day.timeLogOut!!.time - day.timeLogIn!!.time
                val minute = timeDifference / (1000 * 60) % 60
                val hour = timeDifference / (1000 * 60 * 60) % 24
                diff = String.format(Locale.getDefault(), "%02d:%02d", Math.abs(hour), Math.abs(minute));
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = logInDay,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .width(50.dp),
            text = logInName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = logIn,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = logOut,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = diff,
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    val showDay2 = showDay
    showDay = false
    return showDay2
}