package glavni.paket.arbeitszeit.repositories

import androidx.lifecycle.LiveData
import glavni.paket.arbeitszeit.db.*
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(private val periodDao: PeriodDao,
                                         private val dayDao: DayDao,
                                         private val weekDao: WeekDao) {

    fun insertPeriod(period: Period) = periodDao.insertPeriod(period)

    fun deletePeriod(period: Period) = periodDao.deletePeriod(period)

    fun updatePeriod(period: Period) = periodDao.updatePeriod(period)

    val getLastPeriodLive: LiveData<Period> = periodDao.getLastPeriodLive()

    fun getLastPeriod() = periodDao.getLastPeriod()

    fun getAllPeriodsBetweenTwoDateLive(start: Date, end: Date) = periodDao.getAllPeriodsBetweenTwoDateLive(start, end)

    fun getAllPeriodsBetweenTwoDate(start: Date, end: Date) = periodDao.getAllPeriodsBetweenTwoDate(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = periodDao.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = periodDao.isLogOutExistBetweenTwoDate(start, end)

    fun isTwoDateExistBetweenLogInAndLogOut(start: Date, end: Date) = periodDao.isTwoDateExistBetweenLogInAndLogOut(start, end)

    fun numberPeriodBetweenTwoDate(start: Date, end: Date) = periodDao.numberPeriodBetweenTwoDate(start, end)

    fun numberPeriodsInTable() = periodDao.numberPeriodsInTable()

    fun insertDay(day: Day) = dayDao.insertDay(day)

    fun deleteDay(day: Day) = dayDao.deleteDay(day)

    fun updateDay(day: Day) = dayDao.updateDay(day)

    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) = dayDao.isDayLogInExistBetweenTwoDate(start, end)

    fun getDayBetweenTwoDate(start: Date, end: Date) = dayDao.getDayBetweenTwoDate(start, end)

    fun getAllDaysInWeekLive(start: Date, end: Date) = dayDao.getAllDaysInWeekLive(start, end)

    fun getAllDaysInWeek(start: Date, end: Date) = dayDao.getAllDaysInWeek(start, end)

    fun getAllDays() = dayDao.getAllDays()

    fun insertWeek(week: Week) = weekDao.insertWeek(week)

    fun deleteWeek(week: Week) = weekDao.deleteWeek(week)

    fun updateWeek(week: Week) = weekDao.updateWeek(week)

    fun isWeekExist(start: Date) = weekDao.isWeekExist(start)

    fun getWeek(start: Date) = weekDao.getWeek(start)

    fun getWeekLive(start: Date) = weekDao.getWeekLive(start)

    fun getAllWeeksLive() = weekDao.getAllWeeksLive()

    fun getAllWeeks() = weekDao.getAllWeeks()

    fun getSumAllWeeks() = weekDao.getSumAllWeeks()
}