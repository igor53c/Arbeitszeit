package glavni.paket.arbeitszeit.util.hoursscreen

import android.annotation.SuppressLint
import android.util.Log
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
import glavni.paket.arbeitszeit.db.Period
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun animatedListDay(day: Day?): Boolean {
    var upClicked by remember { mutableStateOf(true) }
    val animatedProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 360f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }
    val colorOnSurface = MaterialTheme.colors.onSurface
    var color by remember { mutableStateOf(colorOnSurface) }
    if(day != null && day.workDay != null && day.workDay == false) {
        color = MaterialTheme.colors.primaryVariant
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(shape = RoundedCornerShape(12.dp), color = color)
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer(rotationX = animatedProgress.value)
            .clickable {
                upClicked = false
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        var logIn = ""
        var logOut = ""
        var logInDay = "--"
        var logInName = "---"
        var diff = ""
        if(day != null) {
            if(day.firstLogIn != null) {
                logIn = SimpleDateFormat("HH:mm").format(day.firstLogIn!!)
                logInDay = SimpleDateFormat("dd").format(day.firstLogIn!!)
                logInName = SimpleDateFormat("E").format(day.firstLogIn!!)
            }
            if(day.lastLogOut != null && day.workingTime != null) {
                logOut = SimpleDateFormat("HH:mm").format(day.lastLogOut!!)
                val timeDifference: Long = day.workingTime!!
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
    return upClicked
}