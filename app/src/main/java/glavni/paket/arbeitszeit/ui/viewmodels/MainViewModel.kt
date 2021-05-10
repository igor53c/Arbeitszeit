package glavni.paket.arbeitszeit.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.db.Period
import glavni.paket.arbeitszeit.other.MyPreference
import glavni.paket.arbeitszeit.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository,
    val myPreference: MyPreference
): ViewModel() {

    fun insertPeriod(period: Period) = mainRepository.insertPeriod(period)

    fun deletePeriod(period: Period) = mainRepository.deletePeriod(period)

    fun updatePeriod(period: Period) = mainRepository.updatePeriod(period)

    val getLastPeriod: LiveData<Period> = mainRepository.getLastPeriod

    fun getAllPeriodsInWeek(start: Date, end: Date) = mainRepository.getAllPeriodsInWeek(start, end)

    fun getAllPeriodsInWeek2(start: Date, end: Date) = mainRepository.getAllPeriodsInWeek2(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogOutExistBetweenTwoDate(start, end)

    fun isTwoDateExistBetweenLogInAndLogOut(start: Date, end: Date) = mainRepository.isTwoDateExistBetweenLogInAndLogOut(start, end)

    fun numberPeriodBetweenTwoDate(start: Date, end: Date) = mainRepository.numberPeriodBetweenTwoDate(start, end)

    fun numberPeriodsInTable() = mainRepository.numberPeriodsInTable()

    fun insertDay(day: Day) = mainRepository.insertDay(day)

    fun deleteDay(day: Day) = viewModelScope.launch { mainRepository.deleteDay(day) }

    fun updateDay(day: Day) = viewModelScope.launch { mainRepository.updateDay(day) }

    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isDayLogInExistBetweenTwoDate(start, end)

    fun getDayBetweenTwoDate(start: Date, end: Date) = mainRepository.getDayBetweenTwoDate(start, end)

    fun getAllDaysInWeek(start: Date, end: Date) = mainRepository.getAllDaysInWeek(start, end)
}