package glavni.paket.arbeitszeit.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.other.MyPreference
import glavni.paket.arbeitszeit.repositories.MainRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository,
    val myPreference: MyPreference
): ViewModel() {

    fun insertDay(day: Day) = viewModelScope.launch { mainRepository.insertDay(day) }

    fun deleteDay(day: Day) = viewModelScope.launch { mainRepository.deleteDay(day) }

    fun updateDay(day: Day) = viewModelScope.launch { mainRepository.updateDay(day) }

    val getLastDay: LiveData<Day> = mainRepository.getLastDay

    fun getAllDayInWeek(start: Date, end: Date) = mainRepository.getAllDayInWeek(start, end)

    fun isLogInExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogInExistBetweenTwoDate(start, end)

    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) = mainRepository.isLogOutExistBetweenTwoDate(start, end)
}