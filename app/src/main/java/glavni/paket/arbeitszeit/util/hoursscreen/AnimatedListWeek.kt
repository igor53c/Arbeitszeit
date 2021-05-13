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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glavni.paket.arbeitszeit.db.Week
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@SuppressLint("SimpleDateFormat")
@Composable
fun animatedListWeek(week: Week?): Week? {
    var returnWeek: Week? by remember { mutableStateOf(null) }
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
            .background(
                shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
                brush = Brush.horizontalGradient(
                    colors = listOf(MaterialTheme.colors.secondary, MaterialTheme.colors.secondaryVariant),
                    startX = 0f,
                    endX = 1500f
                ))
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer(rotationX = animatedProgress.value)
            .clickable {
                returnWeek = week
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        var date = ""
        var workingTime = ""
        var balance = ""
        if(week != null) {
            val start = SimpleDateFormat("dd/MM").format(week.start!!)
            val end = SimpleDateFormat("dd/MM/''yy").format(week.end!!)
            date = "$start - $end"
            if(week.workingTime != null) {
                val timeDifference: Long = week.workingTime!!
                val minute = timeDifference / (1000 * 60) % 60
                val hour = timeDifference / (1000 * 60 * 60)
                workingTime = String.format(Locale.getDefault(), "%02d:%02d", abs(hour), abs(minute))
            }
            if(week.balance != null) {
                balance = getLongTimeToString(week.balance!!, true)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = date,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = balance,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = workingTime,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    return returnWeek
}

fun getLongTimeToString(num: Long, plus: Boolean): String {
    val returnString: String
    var timeDifference: Long = num
    if(timeDifference < 0) {
        timeDifference = abs(timeDifference)
        val minute = timeDifference / (1000 * 60) % 60
        val hour = timeDifference / (1000 * 60 * 60)
        returnString = String.format(Locale.getDefault(), "-%02d:%02d", abs(hour), abs(minute))
    } else {
        val minute = timeDifference / (1000 * 60) % 60
        val hour = timeDifference / (1000 * 60 * 60)
        returnString = if(plus) {
            String.format(Locale.getDefault(), "+%02d:%02d", abs(hour), abs(minute))
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", abs(hour), abs(minute))
        }
    }
    return returnString
}