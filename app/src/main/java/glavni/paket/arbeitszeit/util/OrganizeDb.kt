package glavni.paket.arbeitszeit.util

import android.util.Log
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.db.Week
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import glavni.paket.arbeitszeit.util.hoursscreen.getFirstDayOfWeek
import glavni.paket.arbeitszeit.util.hoursscreen.getLastDayOfWeek
import kotlinx.coroutines.*
import java.util.*

@Composable
fun insertInDb(newPeriod: Period, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            val firstDayOfWeek = getFirstDayOfWeek(newPeriod.timeLogIn!!)
            val lastDayOfWeek = getLastDayOfWeek(newPeriod.timeLogIn!!)
            val existsWeek = viewModel.isWeekExist(firstDayOfWeek)
            if(existsWeek != null) {
                if(existsWeek) {
                    insertDayInDb(newPeriod, viewModel)
                    val week = viewModel.getWeek(firstDayOfWeek)
                    if (week != null) {
                        clicked = updateOnlyWeek(week, firstDayOfWeek, lastDayOfWeek, viewModel)
                    }
                } else {
                    if (newPeriod.timeLogOut != null) {
                        insertDayInDb(newPeriod, viewModel)
                        viewModel.insertWeek(Week(firstDayOfWeek, lastDayOfWeek, null, null))
                        val week = viewModel.getWeek(firstDayOfWeek)
                        if (week != null) {
                            clicked = updateOnlyWeek(week, firstDayOfWeek, lastDayOfWeek, viewModel)
                        }
                    } else {
                        insertDayInDb(newPeriod, viewModel)
                        viewModel.insertWeek(Week(firstDayOfWeek, lastDayOfWeek, null, null))
                        clicked = false
                    }
                }
            }
        }
    }
    return clicked
}

fun insertDayInDb(newPeriod: Period, viewModel: MainViewModel): Boolean {
    var clicked = true
    val cal = Calendar.getInstance(Locale.GERMANY)
    val isRounded = viewModel.myPreference.getRounding()
    val breakBelow6 = viewModel.myPreference.getBreakBelow6()
    val break6And9 = viewModel.myPreference.getBreak6And9()
    val breakOver9 = viewModel.myPreference.getBreakOver9()
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
                val day = viewModel.getDayBetweenTwoDate(first, cal.time)
                if (day != null) {
                    clicked = updateOnlyDay(day, first, cal.time, newPeriod.workDay!!, viewModel)
                }
            } else {
                clicked = if (newPeriod.timeLogOut != null) {
                    val workingTime = resultDay(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workingTime,
                        isRounded, breakBelow6, break6And9, breakOver9)
                    viewModel.insertDay(
                        Day(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workDay, workingTime)
                    )
                    viewModel.insertPeriod(newPeriod)
                    false
                } else {
                    viewModel.insertDay(
                        Day(newPeriod.timeLogIn, newPeriod.timeLogOut, newPeriod.workDay, newPeriod.workingTime)
                    )
                    viewModel.insertPeriod(newPeriod)
                    false
                }
            }
        }
    }
    return clicked
}

@Composable
fun updateInDb(oldPeriod: Period, newLogIn: Date?, newLogOut: Date?,
               viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    val cal = Calendar.getInstance(Locale.GERMANY)
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
                    if(!updateOnlyDay(dayInWeek, first, cal.time, oldPeriod.workDay!!, viewModel)) {
                        val firstDayOfWeek = getFirstDayOfWeek(oldPeriod.timeLogIn!!)
                        val lastDayOfWeek = getLastDayOfWeek(oldPeriod.timeLogIn!!)
                        val week = viewModel.getWeek(firstDayOfWeek)
                        if (week != null) {
                            clicked = updateOnlyWeek(week, firstDayOfWeek, lastDayOfWeek, viewModel)
                        }
                    }
                }
            }
        }
    }
    return clicked
}

@Composable
fun deleteData(newPeriod: Period, viewModel: MainViewModel = hiltNavGraphViewModel()): Boolean {
    var clicked = true
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            val numPeriodInTable = viewModel.numberPeriodsInTable()
            if (numPeriodInTable != null) {
                if (numPeriodInTable > 1) {
                    if(newPeriod.timeLogOut == null) {
                        viewModel.myPreference.setLogIn(true)
                    }
                    if (!deleteWeekInDb(newPeriod, viewModel)) {
                        val lastPeriod = viewModel.getLastPeriod()
                        if (lastPeriod?.timeLogIn != null) {
                            viewModel.myPreference.setLastLogIn(lastPeriod.timeLogIn!!.time)
                            clicked = false
                        }
                    }
                } else {
                    if (!deleteWeekInDb(newPeriod, viewModel)) {
                        viewModel.myPreference.setLastLogIn(0)
                        viewModel.myPreference.setLogIn(true)
                        clicked = false
                    }
                }
            }
        }
    }
    return clicked
}

fun deleteWeekInDb(newPeriod: Period, viewModel: MainViewModel): Boolean {
    var clicked = true
    if(newPeriod.timeLogIn != null) {
        val firstDay = getFirstDayOfWeek(newPeriod.timeLogIn!!)
        val lastDay = getLastDayOfWeek(newPeriod.timeLogIn!!)
        val week = viewModel.getWeek(firstDay)
        if(week != null) {
            val numberPeriod = viewModel.numberPeriodBetweenTwoDate(firstDay, lastDay)
            if(numberPeriod != null) {
                deleteDayInDb(newPeriod, viewModel)
                clicked = if(numberPeriod > 1) {
                    updateOnlyWeek(week, firstDay, lastDay, viewModel)
                    false
                } else {
                    viewModel.deleteWeek(week)
                    false
                }
            }
        }
    }
    return clicked
}

fun deleteDayInDb(newPeriod: Period, viewModel: MainViewModel): Boolean {
    var clicked = true
    if(newPeriod.timeLogIn != null) {
        val cal = Calendar.getInstance(Locale.GERMANY)
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
                clicked = if(numberPeriod > 1) {
                    updateOnlyDay(day, first, cal.time, newPeriod.workDay!!, viewModel)
                } else {
                    viewModel.deleteDay(day)
                    false
                }
            }
        }
    }
    return clicked
}

fun resultDay(logIn: Date?, logOut: Date?, total: Long?, isRounded: Boolean,
              breakBelow6: Int, break6And9: Int, breakOver9: Int): Long? {
    var result = total
    if(logIn != null && result!= null && result != 0L) {
        val calLogIn = Calendar.getInstance(Locale.GERMANY)
        calLogIn.time = logIn
        val calLogOut = Calendar.getInstance(Locale.GERMANY)
        if (logOut != null) { calLogOut.time = logOut }
        if (isRounded) {
            if (calLogIn.get(Calendar.MINUTE) % 15 != 0) {
                val diff1 =
                    calLogIn.get(Calendar.MINUTE) + (15 - calLogIn.get(Calendar.MINUTE) % 15)
                result -= (15 - calLogIn.get(Calendar.MINUTE) % 15) * 60 * 1000
                calLogIn.set(Calendar.MINUTE, diff1)
            }
            val diff2: Int
            if (logOut != null) {
                diff2 = calLogOut.get(Calendar.MINUTE) - calLogOut.get(Calendar.MINUTE) % 15
                result -= (calLogOut.get(Calendar.MINUTE) % 15) * 60 * 1000
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

fun updateOnlyWeek(week: Week, start: Date, end: Date, viewModel: MainViewModel): Boolean {
    var clicked = true
    val hoursPerWeek = viewModel.myPreference.getHoursPerWeek()
    val allDays = viewModel.getAllDaysInWeek(start, end)
    if (allDays != null) {
        var total: Long = 0
        for (day in allDays) {
            if (day.workingTime != null) total += day.workingTime!!
        }
        week.workingTime = total
        week.balance = total - (hoursPerWeek * 60 * 60 * 1000).toLong()
        viewModel.updateWeek(week)
        clicked = false
    }
    return clicked
}

fun updateOnlyDay(day: Day, start: Date, end: Date, workDay: Boolean, viewModel: MainViewModel): Boolean {
    var clicked = true
    val isRounded = viewModel.myPreference.getRounding()
    val breakBelow6 = viewModel.myPreference.getBreakBelow6()
    val break6And9 = viewModel.myPreference.getBreak6And9()
    val breakOver9 = viewModel.myPreference.getBreakOver9()
    val allPeriods = viewModel.getAllPeriodsBetweenTwoDate(start, end)
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
        val workingTime = resultDay(
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

fun updateCompleteDb(viewModel: MainViewModel) {
    runBlocking<Unit> {
        launch(Dispatchers.IO) {
            val days = viewModel.getAllDays()
            if (days != null) {
                for (day in days) {
                    updateOnlyDay(day, day.firstLogIn!!, day.lastLogOut!!, day.workDay!!, viewModel)
                }
                val weeks = viewModel.getAllWeeks()
                if(weeks != null) {
                    for(week in weeks) {
                        updateOnlyWeek(week, week.start!!, week.end!!, viewModel)
                    }
                }
            }
        }
    }
}