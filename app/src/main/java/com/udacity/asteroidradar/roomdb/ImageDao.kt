package com.udacity.asteroidradar.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("select * from imagedDb")
    fun getPictureOfDay(): PictureOfDayEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPOD(pictureOfDayEntity: PictureOfDayEntity)
}