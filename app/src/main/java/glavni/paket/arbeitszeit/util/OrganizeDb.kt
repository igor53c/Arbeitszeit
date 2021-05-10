package glavni.paket.arbeitszeit.util

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import kotlinx.coroutines.*
import java.util.*

@Composable
fun insertPeriodInDb(newPeriod: Period, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    val cal = Calendar.getInstance()
    val isRounded = viewModel.myPreference.getRounding()
    val breakBelow6 = viewModel.myPreference.getBreakBelow6()
    val break6And9 = viewModel.myPreference.getBreak6And9()
    val breakOver9 = viewModel.myPreference.getBreakOver9()
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            if(newPeriod.timeLogIn != null) {
                cal.time = newPeriod.timeLogIn!!
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                val first = cal.time
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                val existDay = viewModel.isDayLogInExistBetweenTwoDate(first, cal.time)
                if(existDay != null) {
                    if (existDay) {
                        viewModel.insertPeriod(newPeriod)
                        val dayInWeek = viewModel.getDayBetweenTwoDate(first, cal.time)
                        if (dayInWeek != null) {
                            clicked = updateOnlyDay(dayInWeek, first, cal.time, newPeriod.workDay!!, viewModel)
                        }
                    } else {
                        if (newPeriod.timeLogOut != null) {
                            val workingTime = result(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workingTime,
                                isRounded, breakBelow6, break6And9, breakOver9)
                            viewModel.insertDay(
                                Day(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workDay, workingTime)
                            )
                            viewModel.insertPeriod(newPeriod)
                            clicked = false
                        } else {
                            viewModel.insertDay(
                                Day(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workDay, newPeriod.workingTime)
                            )
                            viewModel.insertPeriod(newPeriod)
                            clicked = false
                        }
                    }
                }
            }
        }
    }
    return clicked
}

@Composable
fun updatePeriodInDb(oldPeriod: Period, newLogIn: Date?, newLogOut: Date?,
                     viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    val cal = Calendar.getInstance()
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            if (oldPeriod.timeLogIn != null) {
                cal.time = oldPeriod.timeLogIn!!
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                val first = cal.time
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                if (newLogIn != null) {
                    if (newLogOut != null) {
                        oldPeriod.timeLogIn = newLogIn
                        oldPeriod.timeLogOut = newLogOut
                        oldPeriod.workingTime = newLogOut.time - newLogIn.time
                    } else {
                        oldPeriod.timeLogIn = newLogIn
                        oldPeriod.timeLogOut = newLogOut
                        oldPeriod.workingTime = null
                    }
                }
                viewModel.updatePeriod(oldPeriod)
                val dayInWeek = viewModel.getDayBetweenTwoDate(first, cal.time)
                if (dayInWeek != null) {
                    clicked = updateOnlyDay(dayInWeek, first, cal.time, oldPeriod.workDay!!, viewModel)
                }
            }
        }
    }
    return clicked
}

@ObsoleteCoroutinesApi
@Composable
fun deletePeriodInDb(newPeriod: Period, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            if(newPeriod.timeLogIn != null) {
                val cal = Calendar.getInstance()
                cal.time = newPeriod.timeLogIn!!
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                val first = cal.time
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                val day = viewModel.getDayBetweenTwoDate(first, cal.time)
                if(day != null) {
                    val numberPeriod = viewModel.numberPeriodBetweenTwoDate(first, cal.time)
                    if(numberPeriod != null) {
                        viewModel.deletePeriod(newPeriod)
                        if(numberPeriod > 1) {
                            clicked = updateOnlyDay(day, first, cal.time, newPeriod.workDay!!, viewModel)
                        } else {
                            viewModel.deleteDay(day)
                            clicked = false
                        }
                    }
                }
            }
        }
    }
    return clicked
}



fun result(logIn: Date?, logOut: Date?, total: Long?, isRounded: Boolean,
           breakBelow6: Int, break6And9: Int, breakOver9: Int): Long? {
    var result = total
    if(logIn != null && result!= null && result != 0L) {
        val calLogIn = Calendar.getInstance()
        calLogIn.time = logIn
        val calLogOut = Calendar.getInstance()
        if (logOut != null) { calLogOut.time = logOut }
        if (isRounded) {
            if (calLogIn.get(Calendar.MINUTE) % 15 != 0) {
                val diff1 =
                    calLogIn.get(Calendar.MINUTE) + (15 - calLogIn.get(Calendar.MINUTE) % 15)
                result -= (15 - calLogIn.get(Calendar.MINUTE) % 15)
                calLogIn.set(Calendar.MINUTE, diff1)
            }
            val diff2: Int
            if (logOut != null) {
                diff2 = calLogOut.get(Calendar.MINUTE) - calLogOut.get(Calendar.MINUTE) % 15
                result -= calLogOut.get(Calendar.MINUTE) % 15
                calLogOut.set(Calendar.MINUTE, diff2)
            }
        }
        if(logOut != null) {
            val dateLogIn = calLogIn.time
            val dateLogOut = calLogOut.time
            val timeDifference = dateLogOut.time - dateLogIn.time
            val pause = timeDifference - result
            if(timeDifference < (1000 * 60 * 60 * 6 + 1000 * 60)) {
                if(pause <= breakBelow6 * 60 * 1000) {
                    result -= breakBelow6 * 60 * 1000 - pause
                }
            } else {
                if(timeDifference < (1000 * 60 * 60 * 9 + 1000 * 60)) {
                    if(pause <= break6And9 * 60 * 1000) {
                        result -= break6And9 * 60 * 1000 - pause
                    }
                } else {
                    if(pause <= breakOver9 * 60 * 1000) {
                        result -= breakOver9 * 60 * 1000 - pause
                    }
                }
            }
        }
    } else {
        result = null
    }
    return result
}

fun updateOnlyDay(day: Day, start: Date, end: Date, workDay: Boolean, viewModel: MainViewModel): Boolean {
    var clicked = true
    val isRounded = viewModel.myPreference.getRounding()
    val breakBelow6 = viewModel.myPreference.getBreakBelow6()
    val break6And9 = viewModel.myPreference.getBreak6And9()
    val breakOver9 = viewModel.myPreference.getBreakOver9()
    val allPeriods = viewModel.getAllPeriodsInWeek2(start, end)
    if (allPeriods != null) {
        var firstElement = true
        var firstLogIn: Date? = null
        var lastLogOut: Date? = null
        var total: Long = 0
        for (per in allPeriods) {
            if (firstElement) {
                lastLogOut = per.timeLogOut
                firstElement = false
            }
            firstLogIn = per.timeLogIn!!
            if (per.workingTime != null) total += per.workingTime!!
        }
        val workingTime = result(
            firstLogIn, lastLogOut, total, isRounded,
            breakBelow6, break6And9, breakOver9
        )
        day.firstLogIn = firstLogIn
        day.lastLogOut = lastLogOut
        day.workDay = workDay
        day.workingTime = workingTime
        viewModel.updateDay(day)
        clicked = false
    }
    return clicked
}
