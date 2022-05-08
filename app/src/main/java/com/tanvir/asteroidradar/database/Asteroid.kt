package com.tanvir.asteroidradar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tanvir.asteroidradar.domain.AsteroidModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "asteroid_info_table")
data class Asteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

fun List<Asteroid>.asDomainModel(): List<AsteroidModel> {
    return map {
        AsteroidModel(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}