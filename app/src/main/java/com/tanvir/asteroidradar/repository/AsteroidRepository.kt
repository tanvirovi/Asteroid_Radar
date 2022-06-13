package com.tanvir.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tanvir.asteroidradar.BuildConfig
import com.tanvir.asteroidradar.GetDate
import com.tanvir.asteroidradar.api.*
import com.tanvir.asteroidradar.database.AsteroidDatabase
import com.tanvir.asteroidradar.database.asDomainModel
import com.tanvir.asteroidradar.domain.AsteroidModel
import com.tanvir.asteroidradar.domain.PodModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidDatabase) {

    val date = GetDate()
    val asteroid: LiveData<List<AsteroidModel>> =
        Transformations.map(database.asteroidDao.getAsteroid()) {
            it.asDomainModel()
        }
    val todayAsteroid: LiveData<List<AsteroidModel>> =
        Transformations.map(database.asteroidDao.getTodayAsteroid(date.startDate)) {
            it.asDomainModel()
        }

    suspend fun refreshData(start: String, end: String) {
        withContext(Dispatchers.IO) {
            try{
                val apiResponse = NasaApi.retrofitService.getNEOAsync(
                    start, end, BuildConfig.API_KEY
                )
                if (apiResponse.isSuccessful) {
                    val convertedJsonObj = JSONObject(apiResponse.body().toString())
                    val asteroidArrayList =
                        parseAsteroidsJsonResult(convertedJsonObj)
                    val asteroidArray =
                        NetworkAsteroidContainer(asteroidArrayList)
                    Timber.tag("asteroidArray").e(asteroidArray.toString())

                    database.asteroidDao.insertAll(*asteroidArray.asDatabaseModel())
                }else{
                    Timber.e("No internet Connection")
                }

            } catch (e: Exception){
                Timber.e(e, e.message)
            }


        }
    }

    val pod: LiveData<List<PodModel>> =
        Transformations.map(database.asteroidDao.getPod()) {
            Timber.tag("poddomain").e(it.toString())
            it.asDomainModel()
        }

    fun deletePod() {
        database.asteroidDao.deletePod()
    }
    fun deleteAsteroid() {
        database.asteroidDao.deleteAsteroid()
    }

    suspend fun refreshPodData() {
        withContext(Dispatchers.IO) {
            try{
                val apiResponse = NasaApi.retrofitService.getPodAsync(
                    BuildConfig.API_KEY
                )
                if (apiResponse.isSuccessful) {
                    val podList = ArrayList<NetworkPod>()
                    podList.add(apiResponse.body()!!)
                    val podArray = PodNetworkContainer(podList)
                    Timber.tag("podArray").e(podArray.toString())

                    database.asteroidDao.insertPod(*podArray.asDataBaseModel())
                }
            }catch (e : Exception){
                Timber.e(e, e.message)

            }
        }
    }
}