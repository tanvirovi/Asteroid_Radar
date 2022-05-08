package com.tanvir.asteroidradar.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.tanvir.asteroidradar.database.Asteroid


//@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroid:  ArrayList<Asteroid>)

//data class NetworkAsteroid(
//    val id: Long,
//    val codename: String,
//    val closeApproachDate: String,
//    val absoluteMagnitude: Double,
//    val estimatedDiameter: Double,
//    val relativeVelocity: Double,
//    val distanceFromEarth: Double,
//    val isPotentiallyHazardous: Boolean
//)

@RequiresApi(Build.VERSION_CODES.N)
fun NetworkAsteroidContainer.asDatabaseModel() : Array<Asteroid> {
    return asteroid.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}