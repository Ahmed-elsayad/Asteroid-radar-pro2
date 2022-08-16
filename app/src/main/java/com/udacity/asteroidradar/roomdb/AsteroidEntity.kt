package com.udacity.asteroidradar.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "asteroidDb")
data class AsteroidEntity constructor(
    // Use the same id as the server
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = "code_name")
    val codename: String,

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_fromEarth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "isPotentially_hazardous")
    val isPotentiallyHazardous: Boolean,
)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
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
    }
}

@Entity(tableName = "imagedDb")
data class PictureOfDayEntity(
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String,
)
//convert db object to domain object
fun PictureOfDayEntity.asDomainModelPicture(): PictureOfDay {
    return PictureOfDay(
        //mediaType = this.mediaType,
        title = this.title,
        url = this.url,
        mediaType = this.mediaType,
    )
}