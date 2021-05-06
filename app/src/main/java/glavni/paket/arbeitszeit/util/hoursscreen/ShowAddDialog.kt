package glavni.paket.arbeitszeit.util.hoursscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import java.util.*

@Composable
fun showAddDialog(showDialog: Boolean, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var showAdd by remember { mutableStateOf(showDialog) }
    val now by remember { mutableStateOf(Calendar.getInstance()) }
    var logInHours by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var logInMinutes by remember { mutableStateOf(now.get(Calendar.MINUTE)) }
    var logInDay by remember { mutableStateOf(now.get(Calendar.DAY_OF_MONTH)) }
    var logInMonth by remember { mutableStateOf(now.get(Calendar.MONTH) + 1) }
    var logInYear by remember { mutableStateOf(now.get(Calendar.YEAR)) }
    var logOutHours by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var logOutMinutes by remember { mutableStateOf(now.get(Calendar.MINUTE)) }
    var errorText by remember { mutableStateOf("") }
    var logInYearErrorState by remember { mutableStateOf(false) }
    var logInMonthErrorState by remember { mutableStateOf(false) }
    var logInDayErrorState by remember { mutableStateOf(false) }
    var logInHoursErrorState by remember { mutableStateOf(false) }
    var logInMinutesErrorState by remember { mutableStateOf(false) }
    var logOutHoursErrorState by remember { mutableStateOf(false) }
    var logOutMinutesErrorState by remember { mutableStateOf(false) }
    var dateLogIn: Date? by remember { mutableStateOf(null) }
    var dateLogOut: Date? by remember { mutableStateOf(null) }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        title = {
            Text(
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
                text = "Add day",
                style = MaterialTheme.typography.body1,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = {
            Column() {
                Row (
                    modifier = Modifier.
                    fillMaxWidth()
                ) {
                    var logInYearString by remember { mutableStateOf(logInYear.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logInYearString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logInYearString = ""
                                    logInYearErrorState = true
                                }
                                test < logInYear - 50 || test > logInYear -> {
                                    logInYearString = test.toString()
                                    logInYearErrorState = true
                                }
                                else -> {
                                    logInYearString = test.toString()
                                    logInYear = test
                                    logInYearErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.33f)
                            .padding(top = 8.dp, end = 4.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Year") },
                        isError = logInYearErrorState
                    )
                    var logInMonthString by remember { mutableStateOf(logInMonth.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logInMonthString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logInMonthString = ""
                                    logInMonthErrorState = true
                                }
                                test < 1 || test > 12 -> {
                                    logInMonthString = test.toString()
                                    logInMonthErrorState = true
                                }
                                else -> {
                                    logInMonthString = test.toString()
                                    logInMonth = test
                                    logInMonthErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.33f)
                            .padding(top = 8.dp, start = 4.dp, bottom = 8.dp, end = 4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Month") },
                        isError = logInMonthErrorState
                    )
                    var logInDayString by remember { mutableStateOf(logInDay.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logInDayString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logInDayString = ""
                                    logInDayErrorState = true
                                }
                                test < 1 || test > 31 -> {
                                    logInDayString = test.toString()
                                    logInDayErrorState = true
                                }
                                else -> {
                                    logInDayString = test.toString()
                                    logInDay = test
                                    logInDayErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.33f)
                            .padding(top = 8.dp, start = 4.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Day") },
                        isError = logInDayErrorState
                    )
                }
                Row (
                    modifier = Modifier.
                    fillMaxWidth()
                ) {
                    var logInHoursString by remember { mutableStateOf(logInHours.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logInHoursString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logInHoursString = ""
                                    logInHoursErrorState = true
                                }
                                test < 0 || test > 23 -> {
                                    logInHoursString = test.toString()
                                    logInHoursErrorState = true
                                }
                                else -> {
                                    logInHoursString = test.toString()
                                    logInHours = test
                                    logInHoursErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Log in hours") },
                        isError = logInHoursErrorState
                    )
                    var logInMinutesString by remember { mutableStateOf(logInMinutes.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logInMinutesString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logInMinutesString = ""
                                    logInMinutesErrorState = true
                                }
                                test < 0 || test > 59 -> {
                                    logInMinutesString = test.toString()
                                    logInMinutesErrorState = true
                                }
                                else -> {
                                    logInMinutesString = test.toString()
                                    logInMinutes = test
                                    logInMinutesErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp, start = 8.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Log in minutes") },
                        isError = logInMinutesErrorState
                    )
                }
                Row (
                    modifier = Modifier.
                    fillMaxWidth()
                ) {
                    var logOutHoursString by remember { mutableStateOf(logOutHours.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logOutHoursString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logOutHoursString = ""
                                    logOutHoursErrorState = true
                                }
                                test < 0 || test > 23 -> {
                                    logOutHoursString = test.toString()
                                    logOutHoursErrorState = true
                                }
                                else -> {
                                    logOutHoursString = test.toString()
                                    logOutHours = test
                                    logOutHoursErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Log out hours") },
                        isError = logOutHoursErrorState
                    )
                    var logOutMinutesString by remember { mutableStateOf(logOutMinutes.toString()) }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        value = logOutMinutesString,
                        onValueChange = {
                            errorText = ""
                            val test = it.toIntOrNull()
                            when {
                                test == null -> {
                                    logOutMinutesString = ""
                                    logOutMinutesErrorState = true
                                }
                                test < 0 || test > 59 -> {
                                    logOutMinutesString = test.toString()
                                    logOutMinutesErrorState = true
                                }
                                else -> {
                                    logOutMinutesString = test.toString()
                                    logOutMinutes = test
                                    logOutMinutesErrorState = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(.5f)
                            .padding(top = 8.dp, start = 8.dp, bottom = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Log out minutes") },
                        isError = logOutMinutesErrorState
                    )
                }
                Text(text = errorText, color = MaterialTheme.colors.error)
            }
        },
        buttons = {
            Row() {
                IconButton(
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 16.dp),
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.
                        fillMaxSize()
                    )
                }
                IconButton(
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = 8.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        if(
                            logInYearErrorState || logInMonthErrorState || logInDayErrorState || logInHoursErrorState ||
                            logInMinutesErrorState || logOutHoursErrorState || logOutMinutesErrorState
                        ) {
                            errorText = "Error"
                        } else {
                            now.setLenient(false)
                            try {
                                now.set(Calendar.YEAR, logInYear)
                                now.set(Calendar.MONTH, logInMonth - 1)
                                now.set(Calendar.DAY_OF_MONTH, logInDay)
                                now.set(Calendar.HOUR_OF_DAY, logInHours)
                                now.set(Calendar.MINUTE, logInMinutes)
                                now.set(Calendar.SECOND, 0)
                                now.set(Calendar.MILLISECOND, 0)
                                dateLogIn = now.time
                                now.set(Calendar.HOUR_OF_DAY, logOutHours)
                                now.set(Calendar.MINUTE, logOutMinutes)
                                dateLogOut = now.time
                            } catch (e: Exception) {
                                errorText = "The Date does not exist!"
                            }
                        }
                    }
                ) {
                    if(dateLogIn != null && dateLogOut != null) {

                        val dateLogInExist =
                            viewModel.isLogInExistBetweenTwoDate(dateLogIn!!, dateLogOut!!).observeAsState().value
                        val dateLogOutExist =
                            viewModel.isLogOutExistBetweenTwoDate(dateLogIn!!, dateLogOut!!).observeAsState().value
                        if(dateLogInExist != null && dateLogOutExist != null) {
                            if(!dateLogInExist && !dateLogOutExist) {
                                if(dateLogIn!!.before(dateLogOut)) {
                                    val newNow = Calendar.getInstance()
                                    if(dateLogIn!!.before(newNow.time) || dateLogOut!!.before(newNow.time)) {
                                        val newDay = Day(dateLogIn, dateLogOut)
                                        viewModel.insertDay(newDay)
                                        dateLogIn = null
                                        dateLogOut = null
                                        showAdd = false
                                    } else {
                                        errorText = "Log in/out can`t be in the future!"
                                    }
                                } else {
                                    errorText = "Log out must be after Log in!"
                                }
                            } else {
                                errorText = "Date overlap!"
                            }
                        }
                    }
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Check",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.
                        fillMaxSize()
                    )
                }
            }
        },
        onDismissRequest = { showAdd = false },
        shape = RoundedCornerShape(16.dp)
    )
    return showAdd
}