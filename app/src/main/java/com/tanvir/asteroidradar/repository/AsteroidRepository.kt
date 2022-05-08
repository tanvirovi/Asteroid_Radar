package com.tanvir.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tanvir.asteroidradar.BuildConfig
import com.tanvir.asteroidradar.api.NasaApi
import com.tanvir.asteroidradar.api.NetworkAsteroidContainer
import com.tanvir.asteroidradar.api.asDatabaseModel
import com.tanvir.asteroidradar.api.parseAsteroidsJsonResult
import com.tanvir.asteroidradar.database.AsteroidDatabase
import com.tanvir.asteroidradar.database.asDomainModel
import com.tanvir.asteroidradar.domain.AsteroidModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase){

    val asteroid: LiveData<List<AsteroidModel>> = Transformations.map(database.asteroidDao.getAsteroid()){
        it.asDomainModel()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshData(start: String, end: String) {
        withContext(Dispatchers.IO){
            val d = NasaApi.retrofitService.getNEO(
                start
                ,end
                ,BuildConfig.API_KEY).await()
            val obj = JSONObject(d)
            val m = parseAsteroidsJsonResult(obj)
            val s = NetworkAsteroidContainer(m)
            database.asteroidDao.insertAll(*s.asDatabaseModel())
        }
    }
}