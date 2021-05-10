package glavni.paket.arbeitszeit.repositories

import androidx.lifecycle.LiveData
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.DayDao
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.db.PeriodDao
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(val periodDao: PeriodDao, val dayDao: DayDao) {

    fun insertPeriod(period: Period) = periodDao.insertPeriod(period)

    fun deletePeriod(period: Period) = periodDao.deletePeriod(period)

    fun updatePeriod(period: Period) = periodDao.updatePeriod(period)

    val getLastPeriod: LiveData<Period> = periodDao.getLastPeriod()

    fun getAllPeriodsInWeek(start: Date, end: Date) = periodDao.getAllPeriodsInWeek(start, end)

    fun getAllPeriodsInWeek2(start: Date, end: Date) = periodDao.getAllPeriodsInWeek2(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = periodDao.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = periodDao.isLogOutExistBetweenTwoDate(start, end)

    fun isTwoDateExistBetweenLogInAndLogOut(start: Date, end: Date) = periodDao.isTwoDateExistBetweenLogInAndLogOut(start, end)

    fun numberPeriodBetweenTwoDate(start: Date, end: Date) = periodDao.numberPeriodBetweenTwoDate(start, end)

    fun numberPeriodsInTable() = periodDao.numberPeriodsInTable()

    fun insertDay(day: Day) = dayDao.insertDay(day)

    suspend fun deleteDay(day: Day) = dayDao.deleteDay(day)

    suspend fun updateDay(day: Day) = dayDao.updateDay(day)

    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) = dayDao.isDayLogInExistBetweenTwoDate(start, end)

    fun getDayBetweenTwoDate(start: Date, end: Date) = dayDao.getDayBetweenTwoDate(start, end)

    fun getAllDaysInWeek(start: Date, end: Date) = dayDao.getAllDaysInWeek(start, end)
}