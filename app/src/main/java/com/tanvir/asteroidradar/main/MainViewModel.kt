package com.tanvir.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.tanvir.asteroidradar.GetDate
import com.tanvir.asteroidradar.database.getDatabase
import com.tanvir.asteroidradar.domain.AsteroidModel
import com.tanvir.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _navigateToFragmentDetails = MutableLiveData<AsteroidModel?>()
    val navigateToFragmentDetails
        get() = _navigateToFragmentDetails

    private val database = getDatabase(app)
    private val asteroidRepo = AsteroidRepository(database)
    val asteroidWeekList = asteroidRepo.asteroid
    val asteroidTodayList = asteroidRepo.todayAsteroid
    val pod = asteroidRepo.pod

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        val date = GetDate()
        viewModelScope.launch {
            asteroidRepo.refreshData(date.startDate, date.endDate)
            asteroidRepo.refreshPodData()
        }
    }

    fun onAsteroidClicked(item: AsteroidModel) {
        _navigateToFragmentDetails.value = item
    }

    fun onFragmentDetailsNavigated() {
        _navigateToFragmentDetails.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}