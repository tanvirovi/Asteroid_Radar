package com.tanvir.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {

    @Query("select * from asteroid_info_table")
    fun getAsteroid(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: Asteroid)
}

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}
