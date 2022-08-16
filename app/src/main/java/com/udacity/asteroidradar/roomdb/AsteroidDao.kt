package com.udacity.asteroidradar.roomdb

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroidEntity: Array<AsteroidEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroidDb ORDER BY close_approach_date DESC")
    fun getAllSavedAsteroids(): List<AsteroidEntity>

    @Update
    fun update(asteroidEntity: AsteroidEntity)

}

