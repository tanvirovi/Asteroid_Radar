package com.tanvir.asteroidradar

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tanvir.asteroidradar.database.getDatabase
import com.tanvir.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params){
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        Timber.tag("executed").e("es")
        val date = GetDate()
        repository.deletePod()
        repository.deleteAsteroid()
        return try {
            repository.refreshData(date.startDate,date.endDate)
            repository.refreshPodData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}