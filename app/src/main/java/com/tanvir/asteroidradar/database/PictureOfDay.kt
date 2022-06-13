package com.tanvir.asteroidradar.database

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.tanvir.asteroidradar.domain.PodModel

@Entity(tableName = "pod_table")
data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    @PrimaryKey
    val url: String
)

fun List<PictureOfDay>.asDomainModel(): List<PodModel> {
    return map {
        PodModel(
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }
}