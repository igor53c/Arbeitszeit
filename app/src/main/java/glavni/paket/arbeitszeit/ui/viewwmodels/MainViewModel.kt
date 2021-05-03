package glavni.paket.arbeitszeit.ui.viewwmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository): ViewModel() {

    val getLastDay: LiveData<Day> = mainRepository.getLastDay

    val getAllDays: LiveData<List<Day>> = mainRepository.getAllDays

    fun insertDay(day: Day) = viewModelScope.launch { mainRepository.insertDay(day) }

    fun deleteDay(day: Day) = viewModelScope.launch { mainRepository.deleteDay(day) }

    fun updateDay(day: Day) = viewModelScope.launch { mainRepository.updateDay(day) }

    fun deleteAllDays() = mainRepository.deleteAllDays()
}