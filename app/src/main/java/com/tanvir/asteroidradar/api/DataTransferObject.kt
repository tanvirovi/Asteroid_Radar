package com.tanvir.asteroidradar.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tanvir.asteroidradar.database.Asteroid
import com.tanvir.asteroidradar.database.PictureOfDay


data class NetworkAsteroidContainer(val asteroid: ArrayList<NetworkAsteroid>)

data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


fun NetworkAsteroidContainer.asDatabaseModel(): Array<Asteroid> {
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

@JsonClass(generateAdapter = true)
data class PodNetworkContainer(val pod: ArrayList<NetworkPod>)

@JsonClass(generateAdapter = true)
data class NetworkPod(
    val date: String,
    val explanation: String,
    val hdurl: String,
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
)

fun PodNetworkContainer.asDataBaseModel(): Array<PictureOfDay> {
    return pod.map {
        PictureOfDay(
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }.toTypedArray()
}