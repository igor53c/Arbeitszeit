package glavni.paket.arbeitszeit.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.db.Week
import glavni.paket.arbeitszeit.other.MyPreference
import glavni.paket.arbeitszeit.repositories.MainRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    val myPreference: MyPreference
): ViewModel() {

    fun insertPeriod(period: Period) = mainRepository.insertPeriod(period)

    fun deletePeriod(period: Period) = mainRepository.deletePeriod(period)

    fun updatePeriod(period: Period) = mainRepository.updatePeriod(period)

    val getLastPeriodLive: LiveData<Period> = mainRepository.getLastPeriodLive

    fun getLastPeriod() = mainRepository.getLastPeriod()

    fun getAllPeriodsBetweenTwoDateLive(start: Date, end: Date) = mainRepository.getAllPeriodsBetweenTwoDateLive(start, end)

    fun getAllPeriodsBetweenTwoDate(start: Date, end: Date) = mainRepository.getAllPeriodsBetweenTwoDate(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogOutExistBetweenTwoDate(start, end)

    fun isTwoDateExistBetweenLogInAndLogOut(start: Date, end: Date) = mainRepository.isTwoDateExistBetweenLogInAndLogOut(start, end)

    fun numberPeriodBetweenTwoDate(start: Date, end: Date) = mainRepository.numberPeriodBetweenTwoDate(start, end)

    fun numberPeriodsInTable() = mainRepository.numberPeriodsInTable()

    fun insertDay(day: Day) = mainRepository.insertDay(day)

    fun deleteDay(day: Day) = mainRepository.deleteDay(day)

    fun updateDay(day: Day) = mainRepository.updateDay(day)

    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isDayLogInExistBetweenTwoDate(start, end)

    fun getDayBetweenTwoDate(start: Date, end: Date) = mainRepository.getDayBetweenTwoDate(start, end)

    fun getAllDaysInWeekLive(start: Date, end: Date) = mainRepository.getAllDaysInWeekLive(start, end)

    fun getAllDaysInWeek(start: Date, end: Date) = mainRepository.getAllDaysInWeek(start, end)

    fun getAllDays() = mainRepository.getAllDays()

    fun insertWeek(week: Week) = mainRepository.insertWeek(week)

    fun deleteWeek(week: Week) = mainRepository.deleteWeek(week)

    fun updateWeek(week: Week) = mainRepository.updateWeek(week)

    fun isWeekExist(start: Date) = mainRepository.isWeekExist(start)

    fun getWeek(start: Date) = mainRepository.getWeek(start)

    fun getWeekLive(start: Date) = mainRepository.getWeekLive(start)

    fun getAllWeeksLive() = mainRepository.getAllWeeksLive()

    fun getAllWeeks() = mainRepository.getAllWeeks()

    fun getSumAllWeeks() = mainRepository.getSumAllWeeks()
}