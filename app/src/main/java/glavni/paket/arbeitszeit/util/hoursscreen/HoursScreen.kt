package glavni.paket.arbeitszeit.util.hoursscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.db.Week
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ObsoleteCoroutinesApi
@SuppressLint("SimpleDateFormat")
@Composable
fun HoursScreen(viewModel: MainViewModel = hiltNavGraphViewModel()) {
    var upClickedDay by remember { mutableStateOf(false)}
    var upClickedWeek by remember { mutableStateOf(false)}
    var returnWeek: Week? by remember { mutableStateOf(null)}
    var returnDay: Day? by remember { mutableStateOf(null)}
    var showDialogForDay by remember { mutableStateOf(false)}
    var showDialogForAdd by remember { mutableStateOf(false)}
    var periodDialog: Period? by remember { mutableStateOf(null)}
    val lastLogInLong by remember { mutableStateOf(viewModel.myPreference.getLastLogIn())}
    val firstDay: Date
    val lastDay: Date
    if(lastLogInLong == 0L) {
        firstDay = getFirstDayOfWeek(Calendar.getInstance(Locale.GERMANY).time)
        lastDay = getLastDayOfWeek(Calendar.getInstance(Locale.GERMANY).time)
    } else {
        firstDay = getFirstDayOfWeek(Date(lastLogInLong))
        lastDay = getLastDayOfWeek(Date(lastLogInLong))
    }
    var firstDayMonth = SimpleDateFormat("MMM").format(firstDay).toString()
    var lastDayMonth = SimpleDateFormat("MMM").format(lastDay).toString()
    var currentWeek = viewModel.getWeekLive(firstDay).observeAsState().value
    val sumBalance = viewModel.getSumAllWeeks().observeAsState().value
    var periods = viewModel.getAllPeriodsBetweenTwoDateLive(firstDay, lastDay).observeAsState().value
    var days = viewModel.getAllDaysInWeekLive(firstDay, lastDay).observeAsState().value
    val weeks = viewModel.getAllWeeksLive().observeAsState().value
    Column {
        if(returnWeek != null) {
            currentWeek = returnWeek
            firstDayMonth = SimpleDateFormat("MMM").format(returnWeek!!.start!!).toString()
            lastDayMonth = SimpleDateFormat("MMM").format(returnWeek!!.end!!).toString()
        }
        val monthString: String = if(firstDayMonth == lastDayMonth) {
            firstDayMonth
        } else {
            "$firstDayMonth/$lastDayMonth"
        }
        val workingTime = currentWeek?.workingTime
        if(showDialogForAdd) {
            showDialogForAdd = showAddDialog(showDialog = showDialogForAdd)
        }
        var sumBalanceString = "0"
        if(sumBalance != null) {
            sumBalanceString = getLongTimeToString(sumBalance, true)
        }
        var workingTimeString = "0"
        if(workingTime != null) {
            workingTimeString = getLongTimeToString(workingTime, false)
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
                    color = MaterialTheme.colors.primary
                )
                ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp)
                    ) {
                Text(
                    modifier = Modifier
                        .weight(.33f),
                    text = monthString,
                    textAlign = TextAlign.Start,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.33f),
                    text = SimpleDateFormat("yyyy").format(firstDay).toString() ,
                    textAlign = TextAlign.Center,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.33f),
                    text = workingTimeString,
                    textAlign = TextAlign.End,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 8.dp)
                    ) {
                IconButton(
                    onClick = {
                        if(upClickedDay) {
                            upClickedWeek = true
                        } else {
                            upClickedDay = true
                        }
                              },
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowCircleUp,
                        contentDescription = "Up",
                        tint = MaterialTheme.colors.background,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp),
                    text = sumBalanceString,
                    textAlign = TextAlign.Center,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = {
                        showDialogForAdd = true
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = MaterialTheme.colors.background,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
        if(showDialogForDay) {
            showDialogForDay = showPeriodDialog(periodDialog = periodDialog, showDayValue = showDialogForDay)
        }
        if(upClickedWeek && weeks != null) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = weeks,
                    itemContent = { _, week ->
                        val ret = animatedListWeek(week)
                        if(ret?.start != null) {
                                upClickedWeek = false
                                returnWeek = ret
                        }
                    }
                )
            }
        }
        if(!upClickedWeek && returnWeek != null) {
            days = viewModel
                .getAllDaysInWeekLive(returnWeek!!.start!!, returnWeek!!.end!!)
                .observeAsState().value
        }
        if(upClickedDay && !upClickedWeek && days != null) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = days!!,
                    itemContent = { _, day ->
                        val ret = animatedListDay(day)
                        if(ret?.firstLogIn != null) {
                                upClickedDay = false
                                returnDay = ret
                        }
                    }
                )
            }
        }
        if(!upClickedDay && returnDay != null) {
            periods = viewModel
                .getAllPeriodsBetweenTwoDateLive(
                    getFirstDayOfWeek(returnDay!!.firstLogIn!!), getLastDayOfWeek(returnDay!!.firstLogIn!!))
                .observeAsState().value
        }
        if(periods != null && !upClickedDay) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = periods!!,
                    itemContent = { _, period ->
                        if(animatedListPeriod(period, showDialogForDay)) {
                            showDialogForDay = true
                            periodDialog = period
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(58.dp))
    }
}

fun getFirstDayOfWeek(date: Date): Date {
    val cal = Calendar.getInstance(Locale.GERMANY)
    cal.time = date
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    return cal.time
}

fun getLastDayOfWeek(date: Date): Date {
    val cal = Calendar.getInstance(Locale.GERMANY)
    cal.time = date
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    return cal.time
}
