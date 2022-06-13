package com.tanvir.asteroidradar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AsteroidModel (
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean): Parcelable