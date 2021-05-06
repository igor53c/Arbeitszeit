package glavni.paket.arbeitszeit.repositories

import androidx.lifecycle.LiveData
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.DayDao
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(val dayDao: DayDao) {

    val getLastDay: LiveData<Day> = dayDao.getLastDay()

    val getAllDays: LiveData<List<Day>> = dayDao.getAllDays()

    fun getAllDayInWeek(start: Date, end: Date) = dayDao.getAllDayInWeek(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = dayDao.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = dayDao.isLogOutExistBetweenTwoDate(start, end)

    suspend fun insertDay(day: Day) = dayDao.insertDay(day)

    suspend fun deleteDay(day: Day) = dayDao.deleteDay(day)

    suspend fun updateDay(day: Day) = dayDao.updateDay(day)

    fun deleteAllDays() = dayDao.deleteAllDays()
}