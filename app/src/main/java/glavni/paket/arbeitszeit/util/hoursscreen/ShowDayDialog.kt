package glavni.paket.arbeitszeit.util.hoursscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun showDayDialog(dayDialog: Day?, showDayValue: Boolean, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var errorText by remember { mutableStateOf("") }
    val now by remember { mutableStateOf(Calendar.getInstance()) }
    var showDay by remember { mutableStateOf(showDayValue) }
    var delete by remember { mutableStateOf(false) }
    var update by remember { mutableStateOf(false) }
    var logOutChanged by remember { mutableStateOf(false) }
    var logInHoursErrorState by remember { mutableStateOf(false) }
    var logInMinutesErrorState by remember { mutableStateOf(false) }
    var logOutHoursErrorState by remember { mutableStateOf(false) }
    var logOutMinutesErrorState by remember { mutableStateOf(false) }
    var logInHours = 0
    var logInMinutes = 0
    var logOutHours = 0
    var logOutMinutes = 0
    var logInDay = ""
    var logInName = ""
    var logInYear = ""
    var logInMonth = ""
    if(dayDialog != null) {
        if(dayDialog.timeLogIn != null) {
            val cal = Calendar.getInstance()
            cal.time = dayDialog.timeLogIn!!
            logInHours = cal.get(Calendar.HOUR_OF_DAY)
            logInMinutes = cal.get(Calendar.MINUTE)
            logInDay = SimpleDateFormat("dd").format(dayDialog.timeLogIn!!)
            logInName = SimpleDateFormat("E").format(dayDialog.timeLogIn!!)
            logInMonth = SimpleDateFormat("MMM").format(dayDialog.timeLogIn!!)
            logInYear = SimpleDateFormat("yyyy").format(dayDialog.timeLogIn!!)
        }
        if(dayDialog.timeLogOut != null) {
            val cal = Calendar.getInstance()
            cal.time = dayDialog.timeLogOut!!
            logOutHours = cal.get(Calendar.HOUR_OF_DAY)
            logOutMinutes = cal.get(Calendar.MINUTE)
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
                            logOutChanged = true
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
                            logOutChanged = true
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
                        .weight(.33f)
                        .padding(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 16.dp),
                    onClick = {
                        delete = true
                    }
                ) {
                    if(delete && dayDialog != null) {
                        if(dayDialog.timeLogOut != null) {
                            viewModel.deleteDay(dayDialog)
                            if(dayDialog.timeLogIn?.time == viewModel.myPreference.getLastLogIn()) {
                                val lastDay = viewModel.getLastDay.observeAsState().value
                                if(lastDay != null) {
                                    lastDay.timeLogIn?.let { viewModel.myPreference.setLastLogIn(it.time) }
                                    delete = false
                                    showDay = false
                                }
                            } else {
                                showDay = false
                                delete = false
                            }
                        } else {
                            viewModel.deleteDay(dayDialog)
                            val lastDay = viewModel.getLastDay.observeAsState().value
                            viewModel.myPreference.setLogIn(true)
                            if(lastDay != null) {
                                lastDay.timeLogIn?.let { viewModel.myPreference.setLastLogIn(it.time) }
                                delete = false
                                showDay = false
                            }
                        }
                    }
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.
                        fillMaxSize()
                    )
                }
                IconButton(
                    modifier = Modifier
                        .weight(.33f)
                        .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 16.dp),
                    onClick = {
                        showDay = false
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
                        .weight(.33f)
                        .padding(start = 8.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        update = true
                    }
                ) {
                    if(update && dayDialog != null && dayDialog.timeLogIn != null) {
                        if (
                            logInHoursErrorState || logInMinutesErrorState ||
                            logOutHoursErrorState || logOutMinutesErrorState
                        ) {
                            errorText = "Error"
                        } else {
                            now.time = dayDialog.timeLogIn!!
                            now.set(Calendar.HOUR_OF_DAY, logInHours)
                            now.set(Calendar.MINUTE, logInMinutes)
                            val dateLogIn = now.time
                            now.set(Calendar.HOUR_OF_DAY, logOutHours)
                            now.set(Calendar.MINUTE, logOutMinutes)
                            val dateLogOut = now.time
                            if(dayDialog.timeLogOut != null) {
                                val dateLogInExist = viewModel
                                    .isLogInExistBetweenTwoDate(dateLogOut, dayDialog.timeLogOut!!).observeAsState().value
                                val dateLogOutExist = viewModel
                                    .isLogOutExistBetweenTwoDate(dateLogIn, dayDialog.timeLogIn!!).observeAsState().value
                                if(dateLogInExist != null && dateLogOutExist != null) {
                                    if(!dateLogInExist && !dateLogOutExist) {
                                        if(dateLogIn.before(dateLogOut)) {
                                            val newNow = Calendar.getInstance()
                                            if(dateLogIn.before(newNow.time) || dateLogOut.before(newNow.time)) {
                                                dayDialog.timeLogIn = dateLogIn
                                                dayDialog.timeLogOut = dateLogOut
                                                viewModel.updateDay(dayDialog)
                                                update = false
                                                showDay = false
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
                            } else {
                                if(logOutChanged) {
                                    val dateLogInExist = viewModel
                                        .isLogInExistBetweenTwoDate(dayDialog.timeLogIn!!, dateLogOut).observeAsState().value
                                    val dateLogOutExist = viewModel
                                        .isLogOutExistBetweenTwoDate(dateLogIn, dayDialog.timeLogIn!!).observeAsState().value
                                    if(dateLogInExist != null && dateLogOutExist != null) {
                                        if(!dateLogInExist && !dateLogOutExist) {
                                            if(dateLogIn.before(dateLogOut)) {
                                                val newNow = Calendar.getInstance()
                                                if(dateLogIn.before(newNow.time) || dateLogOut.before(newNow.time)) {
                                                    dayDialog.timeLogIn = dateLogIn
                                                    dayDialog.timeLogOut = dateLogOut
                                                    viewModel.updateDay(dayDialog)
                                                    viewModel.myPreference.setLogIn(true)
                                                    viewModel.myPreference.setLastLogIn(dateLogIn.time)
                                                    update = false
                                                    showDay = false
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
                                } else {
                                    val dateLogOutExist = viewModel
                                        .isLogOutExistBetweenTwoDate(dateLogIn, dayDialog.timeLogIn!!).observeAsState().value
                                    if(dateLogOutExist != null) {
                                        if(!dateLogOutExist) {
                                            val newNow = Calendar.getInstance()
                                            if(dateLogIn.before(newNow.time)) {
                                                dayDialog.timeLogIn = dateLogIn
                                                viewModel.updateDay(dayDialog)
                                                update = false
                                                showDay = false
                                            } else {
                                                errorText = "Log in can`t be in the future!"
                                            }
                                        } else {
                                            errorText = "Date overlap!"
                                        }
                                    }
                                }
                            }

                        }
                    }
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Update",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        onDismissRequest = { showDay = false },
        shape = RoundedCornerShape(16.dp)
    )
    return showDay
}