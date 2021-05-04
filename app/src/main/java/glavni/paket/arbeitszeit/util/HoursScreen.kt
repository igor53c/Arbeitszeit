package glavni.paket.arbeitszeit.util

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.LifecycleOwner
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.theme.RedInit
import glavni.paket.arbeitszeit.ui.theme.RedInitLight
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HoursScreen(viewModel: MainViewModel = hiltNavGraphViewModel()) {
    val days = viewModel.getAllDays.observeAsState().value
    val buttons = listOf("2021", "", "+7:00", "April", "Week 15", "35:00")
    Column {
        var animationIndex by remember { mutableStateOf(0) }
        VerticalGrid(columns = 3, modifier = Modifier.padding(vertical = 4.dp)) {
            buttons.forEachIndexed { index, title ->
                ButtonChip(
                    selected = index == animationIndex,
                    text = title,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable(onClick = {
                            animationIndex = index
                        })
                )
            }
        }
        if(days != null) {
            LazyColumn ( modifier = Modifier.weight(1f)) {
                itemsIndexed(
                    items = days,
                    itemContent = { index, day ->
                        AnimatedListItem(day = day)
                    })
            }
        }
        Spacer(modifier = Modifier.height(58.dp))
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun AnimatedListItem(day: Day?) {
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
            .background(shape = RoundedCornerShape(12.dp), color = RedInitLight)
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer(rotationX = animatedProgress.value)
            .clickable { }
            .border(3.dp, color = RedInit, shape = RoundedCornerShape(12.dp)),
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
            style = typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .width(50.dp),
            text = logInName,
            textAlign = TextAlign.Center,
            style = typography.body1,
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
                style = typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = logOut,
                style = typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.5f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = diff,
            style = typography.body1,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        // Keep given height constraints, but set an exact width
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        // Measure each item with these constraints
        val placeables = measurables.map { it.measure(itemConstraints) }
        // Track each columns height so we can calculate the overall height
        val columnHeights = Array(columns) { 0 }
        placeables.forEachIndexed { index, placeable ->
            val column = index % columns
            columnHeights[column] += placeable.height
        }
        val height = (columnHeights.maxOrNull() ?: constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // Track the Y co-ord per column we have placed up to
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                placeable.place(
                    x = column * itemWidth,
                    y = columnY[column]
                )
                columnY[column] += placeable.height
            }
        }
    }
}
@Composable
fun ButtonChip(selected: Boolean, text: String, modifier: Modifier = Modifier) {
    Surface(
        color = RedInitLight,
        contentColor = Color.Black,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 3.dp,
            color = RedInit
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = typography.body2,
            modifier = Modifier.padding(8.dp)
        )

    }
}