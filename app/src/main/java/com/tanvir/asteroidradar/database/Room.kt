package com.tanvir.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {
    @Query("select * from asteroid_info_table order by closeApproachDate asc")
    fun getAsteroid(): LiveData<List<Asteroid>>

    @Query("select * from asteroid_info_table where closeApproachDate == :date")
    fun getTodayAsteroid(date: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: Asteroid)

    @Query("select * from pod_table")
    fun getPod(): LiveData<List<PictureOfDay>>

    @Query("delete from pod_table")
    fun deletePod()

    @Query("delete from asteroid_info_table")
    fun deleteAsteroid()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPod(vararg pod: PictureOfDay)
}

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
