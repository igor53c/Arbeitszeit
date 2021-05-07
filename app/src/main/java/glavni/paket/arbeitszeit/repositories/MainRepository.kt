package glavni.paket.arbeitszeit.repositories

import androidx.lifecycle.LiveData
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.DayDao
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(val dayDao: DayDao) {

    suspend fun insertDay(day: Day) = dayDao.insertDay(day)

    suspend fun deleteDay(day: Day) = dayDao.deleteDay(day)

    suspend fun updateDay(day: Day) = dayDao.updateDay(day)

    val getLastDay: LiveData<Day> = dayDao.getLastDay()

    fun getAllDayInWeek(start: Date, end: Date) = dayDao.getAllDayInWeek(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = dayDao.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = dayDao.isLogOutExistBetweenTwoDate(start, end)

}