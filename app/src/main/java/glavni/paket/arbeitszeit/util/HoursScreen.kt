package glavni.paket.arbeitszeit.util

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.MainActivity
import glavni.paket.arbeitszeit.ui.theme.RedInit
import glavni.paket.arbeitszeit.ui.theme.RedInitLight
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun HoursScreen(viewModel: MainViewModel = hiltNavGraphViewModel()) {
    var showDay by remember { mutableStateOf(false)}
    var showCalendar by remember { mutableStateOf(false)}
    var dayDialog: Day? by remember { mutableStateOf(null)}
    val lastLogInLong by remember { mutableStateOf(viewModel.myPreference.getLastLogIn())}
    val lastLogIn = lastLogInLong.let { Date(it) }
    val days = viewModel.getAllDayInWeek(
        getFirstDayOfWeek(lastLogIn),
        getLastDayOfWeek(lastLogIn)
    ).observeAsState().value
    Column {
        if(showCalendar)
            AlertDialog(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                title = {
                    TextButton(
                    onClick = { showCalendar = false }
                ) {
                    Text(
                        text = "Select week or cancel!",
                        style = typography.h6,
                        textAlign = TextAlign.Center
                    )
                }},
                text = {
                    if(days != null) {
                        LazyColumn ( modifier = Modifier.weight(1f)) {
                            itemsIndexed(
                                items = days,
                                itemContent = { index, day ->
                                    AnimatedListWeek(day = day)
                                })
                        }
                    }
                },
                buttons = {},
                onDismissRequest = { showCalendar = false },
                shape = RoundedCornerShape(16.dp)
            )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = RedInit
                )
                .clickable {
                    showCalendar = true
                }
                ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp)
                    ) {
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = SimpleDateFormat("yyyy").format(lastLogIn).toString() ,
                    textAlign = TextAlign.Start,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = "",
                    textAlign = TextAlign.Center,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = "+7,00",
                    textAlign = TextAlign.End,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 8.dp)
                    ) {
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = SimpleDateFormat("MMM").format(lastLogIn).toString(),
                    textAlign = TextAlign.Start,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = "Week " + SimpleDateFormat("ww").format(lastLogIn).toString() ,
                    textAlign = TextAlign.Center,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.3f),
                    text = "40,00",
                    textAlign = TextAlign.End,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if(showDay) {
            var logIn = ""
            var logOut = ""
            var logInDay = "--"
            var logInName = "---"
            var diff = ""
            var logInYear = ""
            var logInMonth = ""
            if(dayDialog != null) {
                if(dayDialog?.timeLogIn != null) {
                    logIn = SimpleDateFormat("HH:mm").format(dayDialog?.timeLogIn!!)
                    logInDay = SimpleDateFormat("dd").format(dayDialog?.timeLogIn!!)
                    logInName = SimpleDateFormat("E").format(dayDialog?.timeLogIn!!)
                    logInMonth = SimpleDateFormat("MMM").format(dayDialog?.timeLogIn!!)
                    logInYear = SimpleDateFormat("yyyy").format(dayDialog?.timeLogIn!!)
                }
                if(dayDialog?.timeLogOut != null) {
                    logOut = SimpleDateFormat("HH:mm").format(dayDialog?.timeLogOut!!)
                    val timeDifference = dayDialog?.timeLogOut!!.time - dayDialog?.timeLogIn!!.time
                    val minute = timeDifference / (1000 * 60) % 60
                    val hour = timeDifference / (1000 * 60 * 60) % 24
                    diff = String.format(Locale.getDefault(),
                        "%02d:%02d", Math.abs(hour), Math.abs(minute));
                }
            }
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
                        text = logInName + " " + logInDay + " " + logInMonth + " " + logInYear,
                        style = typography.body1,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    },
                text = {
                    Column() {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Log in: $logIn",
                            style = typography.body1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Log out: $logOut",
                            style = typography.body1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Work: $diff",
                            style = typography.body1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                buttons = {
                    Row() {
                        Button(
                            modifier = Modifier
                                .weight(.5f)
                                .padding(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 16.dp)
                                .background(
                                    shape = RoundedCornerShape(16.dp),
                                    color = RedInit
                                ),
                            onClick = {
                                showDay = false
                            }
                        ) {
                            Text(
                                text = "Delete",
                                style = typography.h6
                            )
                        }
                        Button(
                            modifier = Modifier
                                .weight(.5f)
                                .padding(start = 8.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
                                .background(
                                    shape = RoundedCornerShape(16.dp),
                                    color = RedInit
                                ),
                            onClick = {
                                showDay = false
                            }
                        ) {
                            Text(
                                text = "Ok",
                                style = typography.h6
                            )
                        }
                    }
                },
                onDismissRequest = { showDay = false },
                shape = RoundedCornerShape(16.dp)
            )
        }
        if(days != null) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = days,
                    itemContent = { index, day ->
                        if(animatedListDay(day = day, showDay)) {
                            showDay = true
                            dayDialog = day
                        }
                    })
            }
        }
        Spacer(modifier = Modifier.height(58.dp))
    }
}

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

@SuppressLint("SimpleDateFormat")
@Composable
fun animatedListDay(day: Day?, state: Boolean): Boolean {
    var showDay by remember { mutableStateOf(state)}
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
            .clickable {
                showDay = true
            }
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
    val showDay2 = showDay
    showDay = false
    return showDay2
}

fun getFirstDayOfWeek(date: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    return cal.time
}

fun getLastDayOfWeek(date: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 7)
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    return cal.time
}
