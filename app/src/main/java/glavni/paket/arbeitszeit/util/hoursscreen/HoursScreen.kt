package glavni.paket.arbeitszeit.util

import android.annotation.SuppressLint
import android.util.Log
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
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import glavni.paket.arbeitszeit.util.hoursscreen.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun HoursScreen(viewModel: MainViewModel = hiltNavGraphViewModel()) {
    var upClicked by remember { mutableStateOf(false)}
    var showDialogForDay by remember { mutableStateOf(false)}
    var showDialogForAdd by remember { mutableStateOf(false)}
    var periodDialog: Period? by remember { mutableStateOf(null)}
    val lastLogInLong by remember { mutableStateOf(viewModel.myPreference.getLastLogIn())}
    val lastLogIn = lastLogInLong.let { Date(it) }
    val periods = viewModel.getAllPeriodsInWeek(
        getFirstDayOfWeek(lastLogIn),
        getLastDayOfWeek(lastLogIn)
    ).observeAsState().value
    val days = viewModel.getAllDaysInWeek(
        getFirstDayOfWeek(lastLogIn),
        getLastDayOfWeek(lastLogIn)
    ).observeAsState().value
    Column {
        if(showDialogForAdd) {
            showDialogForAdd = showAddDialog(showDialog = showDialogForAdd)
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
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
                    text = SimpleDateFormat("MMM").format(lastLogIn).toString(),
                    textAlign = TextAlign.Start,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.33f),
                    text = SimpleDateFormat("yyyy").format(lastLogIn).toString() ,
                    textAlign = TextAlign.Center,
                    style = typography.body1,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(.33f),
                    text = "40,00",
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
                    onClick = { upClicked = true },
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
                    text = "+7,00",
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
            showDialogForDay = showDayDialog(periodDialog = periodDialog, showDayValue = showDialogForDay)
        }
        if(upClicked && days != null) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = days,
                    itemContent = { index, day ->
                        upClicked = animatedListDay(day)
                    })
            }
        }
        if(periods != null && !upClicked) {
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(
                    items = periods,
                    itemContent = { index, period ->
                        if(animatedListPeriod(period = period, showDialogForDay)) {
                            showDialogForDay = true
                            periodDialog = period
                        }
                    })
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
