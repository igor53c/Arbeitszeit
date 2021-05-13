package glavni.paket.arbeitszeit.util.hoursscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import glavni.paket.arbeitszeit.util.insertInDb
import java.util.*

@Composable
fun showAddDialog(showDialog: Boolean, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var showAdd by remember { mutableStateOf(showDialog) }
    val now by remember { mutableStateOf(Calendar.getInstance()) }
    var logInHours by remember { mutableStateOf(0) }
    var logInMinutes by remember { mutableStateOf(0) }
    var logInDay by remember { mutableStateOf(now.get(Calendar.DAY_OF_MONTH)) }
    var logInMonth by remember { mutableStateOf(now.get(Calendar.MONTH) + 1) }
    var logInYear by remember { mutableStateOf(now.get(Calendar.YEAR)) }
    var logOutHours by remember { mutableStateOf(0) }
    var logOutMinutes by remember { mutableStateOf(0) }
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
    var selectedDay by remember { mutableStateOf("Work day") }
    var workDay by remember { mutableStateOf(true) }
    var onlyHours by remember { mutableStateOf(0) }
    var onlyMinutes by remember { mutableStateOf(0) }
    var onlyHoursErrorState by remember { mutableStateOf(false) }
    var onlyMinutesErrorState by remember { mutableStateOf(false) }

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
            Column {
                Row (
                    modifier = Modifier.
                    fillMaxWidth()
                ) {
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
                            .padding(top = 8.dp, start = 4.dp, bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Day") },
                        isError = logInDayErrorState
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
                            .padding(top = 8.dp, start = 4.dp, bottom = 16.dp, end = 4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Month") },
                        isError = logInMonthErrorState
                    )
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
                            .padding(top = 8.dp, end = 4.dp, bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Year") },
                        isError = logInYearErrorState
                    )

                }
                if(workDay) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(.5f)
                        ) {
                            Text(text = "Log in:")
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                        .padding(end = 2.dp, bottom = 16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text(text = "Hour") },
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
                                        .padding(start = 2.dp, end = 2.dp, bottom = 16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text(text = "Min.") },
                                    isError = logInMinutesErrorState
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(.5f)
                        ) {
                            Text(text = "Log out:")
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                        .padding(start = 2.dp, end = 2.dp, bottom = 8.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text(text = "Hour") },
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
                                        .padding(start = 2.dp, bottom = 8.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text(text = "Min.") },
                                    isError = logOutMinutesErrorState
                                )
                            }
                        }
                    }
                } else {
                    Row (
                        modifier = Modifier.
                        fillMaxWidth()
                    ) {
                        var onlyHoursString by remember { mutableStateOf(onlyHours.toString()) }
                        OutlinedTextField(
                            singleLine = true,
                            maxLines = 1,
                            value = onlyHoursString,
                            onValueChange = {
                                errorText = ""
                                val test = it.toIntOrNull()
                                when {
                                    test == null -> {
                                        onlyHoursString = ""
                                        onlyHoursErrorState = true
                                    }
                                    test < 0 || test > 23 -> {
                                        onlyHoursString = test.toString()
                                        onlyHoursErrorState = true
                                    }
                                    else -> {
                                        onlyHoursString = test.toString()
                                        onlyHours = test
                                        onlyHoursErrorState = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(.5f)
                                .padding(end = 8.dp, bottom = 16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text(text = "Hours") },
                            isError = onlyHoursErrorState
                        )
                        var onlyMinutesString by remember { mutableStateOf(onlyMinutes.toString()) }
                        OutlinedTextField(
                            singleLine = true,
                            maxLines = 1,
                            value = onlyMinutesString,
                            onValueChange = {
                                errorText = ""
                                val test = it.toIntOrNull()
                                when {
                                    test == null -> {
                                        onlyMinutesString = ""
                                        onlyMinutesErrorState = true
                                    }
                                    test < 0 || test > 59 -> {
                                        onlyMinutesString = test.toString()
                                        onlyMinutesErrorState = true
                                    }
                                    else -> {
                                        onlyMinutesString = test.toString()
                                        onlyMinutes = test
                                        onlyMinutesErrorState = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(.5f)
                                .padding(start = 8.dp, bottom = 16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text(text = "Minutes") },
                            isError = onlyMinutesErrorState
                        )
                    }
                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(.33f)
                    ) {
                        Text(
                            text = "Work day",
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        selectedDay = "Work day"
                                        workDay = true
                                    })
                        )
                        RadioButton(
                            selected = selectedDay == "Work day",
                            onClick = {
                                selectedDay = "Work day"
                                workDay = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(.33f)
                    ) {
                        Text(
                            text = "Vacation",
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        selectedDay = "Vacation"
                                        workDay = false
                                    })
                        )
                        RadioButton(
                            selected = selectedDay == "Vacation",
                            onClick = {
                                selectedDay = "Vacation"
                                workDay = false
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(.33f)
                    ) {
                        Text(
                            text = "Sick leave",
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        selectedDay = "Sick leave"
                                        workDay = false
                                    })
                        )
                        RadioButton(
                            selected = selectedDay == "Sick leave",
                            onClick = {
                                selectedDay = "Sick leave"
                                workDay = false
                            }
                        )
                    }
                }
                Text(text = errorText, color = MaterialTheme.colors.error)
            }
        },
        buttons = {
            Row {
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
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = 8.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        if(workDay) {
                            if(
                                logInYearErrorState || logInMonthErrorState || logInDayErrorState || logInHoursErrorState ||
                                logInMinutesErrorState || logOutHoursErrorState || logOutMinutesErrorState
                            ) {
                                errorText = "Error"
                            } else {
                                now.isLenient = false
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
                        } else {
                            if(
                                logInYearErrorState || logInMonthErrorState || logInDayErrorState
                                || onlyHoursErrorState || onlyMinutesErrorState
                            ) {
                                errorText = "Error"
                            } else {
                                now.isLenient = false
                                try {
                                    now.set(Calendar.YEAR, logInYear)
                                    now.set(Calendar.MONTH, logInMonth - 1)
                                    now.set(Calendar.DAY_OF_MONTH, logInDay)
                                    now.set(Calendar.HOUR_OF_DAY, 8)
                                    now.set(Calendar.MINUTE, 0)
                                    now.set(Calendar.SECOND, 0)
                                    now.set(Calendar.MILLISECOND, 0)
                                    dateLogIn = now.time
                                    now.set(Calendar.HOUR_OF_DAY, onlyHours + 8)
                                    now.set(Calendar.MINUTE, onlyMinutes)
                                    dateLogOut = now.time
                                } catch (e: Exception) {
                                    errorText = "The Date does not exist!"
                                }
                            }
                        }
                    }
                ) {
                    if(dateLogIn != null && dateLogOut != null) {
                        val dateLogInExist =
                            viewModel.isLogInExistBetweenTwoDate(dateLogIn!!, dateLogOut!!).observeAsState().value
                        val dateLogOutExist =
                            viewModel.isLogOutExistBetweenTwoDate(dateLogIn!!, dateLogOut!!).observeAsState().value
                        val twoDate =
                            viewModel.isTwoDateExistBetweenLogInAndLogOut(dateLogIn!!, dateLogOut!!).observeAsState().value
                        if(dateLogIn != null && dateLogOut != null && dateLogInExist != null
                            && dateLogOutExist != null && twoDate != null) {
                            if(!dateLogInExist && !dateLogOutExist && !twoDate) {
                                if(dateLogIn!!.before(dateLogOut)) {
                                    val newNow = Calendar.getInstance()
                                    if(dateLogIn!!.before(newNow.time) || dateLogOut!!.before(newNow.time)) {
                                        val lastLogIn = Date(viewModel.myPreference.getLastLogIn())
                                        if(dateLogOut!!.before(lastLogIn)) {
                                            val workingTime = dateLogOut!!.time - dateLogIn!!.time
                                            val newPeriod = Period(dateLogIn, dateLogOut, workDay, workingTime)
                                            val clicked = insertInDb(newPeriod)
                                            if(!clicked) {
                                                dateLogIn = null
                                                dateLogOut = null
                                                showAdd = false
                                            }
                                        } else {
                                            if(viewModel.myPreference.getLogIn()) {
                                                val workingTime = dateLogOut!!.time - dateLogIn!!.time
                                                val newPeriod = Period(dateLogIn, dateLogOut, workDay, workingTime)
                                                val clicked = insertInDb(newPeriod)
                                                if(!clicked) {
                                                    dateLogIn = null
                                                    dateLogOut = null
                                                    showAdd = false
                                                }
                                            } else {
                                                errorText = "You have to log out first!"
                                            }
                                        }
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
                        contentDescription = "Add",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        onDismissRequest = { showAdd = false },
        shape = RoundedCornerShape(16.dp)
    )
    return showAdd
}