package glavni.paket.arbeitszeit.repositories

import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.DayDao
import javax.inject.Inject

class MainRepository @Inject constructor(val dayDao: DayDao) {

    suspend fun insertDay(day: Day) = dayDao.insertDay(day)

    suspend fun deleteDay(day: Day) = dayDao.deleteDay(day)

    suspend fun updateDay(day: Day) = dayDao.updateDay(day)

    fun getLastDay() = dayDao.getLastDay()

    fun deleteAllDays() = dayDao.deleteAllDays()

    fun getAllDays() = dayDao.getAllDays()
}