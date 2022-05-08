package com.tanvir.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.tanvir.asteroidradar.Constants
import com.tanvir.asteroidradar.database.getDatabase
import com.tanvir.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class NasaApiStatus { LOADING, ERROR, DONE }

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val database = getDatabase(app)
    private val asteroidRepo = AsteroidRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        val startDate = getCurrentDate()
        val endDate = getCalculatedDate(startDate,Constants.API_QUERY_DATE_FORMAT,7)
        viewModelScope.launch {
            asteroidRepo.refreshData(startDate, endDate)
        }
    }

    val playlist = asteroidRepo.asteroid

    @SuppressLint("WeekBasedYear")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate(): String {
        val cal = Calendar.getInstance()
        val currentTime = cal.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        Log.e("Current Date", dateFormat.format(currentTime))
        return dateFormat.format(currentTime)
    }

    fun getCalculatedDate(date: String, dateFormat: String, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        if (date.isNotEmpty()) {
            cal.time = s.parse(date) as Date
        }
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
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