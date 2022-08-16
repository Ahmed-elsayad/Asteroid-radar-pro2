package com.udacity.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.TAG
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.sevenDaysAfter
import com.udacity.asteroidradar.api.today
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.roomdb.AsteroidDatabase
import com.udacity.asteroidradar.roomdb.asDatabaseModel
import com.udacity.asteroidradar.roomdb.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    // using live data transformation to map data from entityclass to model class
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), sevenDaysAfter())
        ) { it.asDomainModel() }

    // Call picture of day from database and  set the data as domain model
    // using live data transformation to map data from entityclass to model class

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(
            database.pictureOfDayDao().getPictureOfDay()
        ) { it?.asDomainModel() }

    // using live data transformation to map data from entityclass to model class

    val todayOnlyAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), today())
        ) { it.asDomainModel() }


    // Call the asteroids from network then delete previous days and insert the data to roomdb
    suspend fun refreshAsteroids(startDate: String = today(), endDate: String = sevenDaysAfter()) {
        var asteroidList: ArrayList<Asteroid>

        withContext(Dispatchers.IO) {
            try {
                val response =
                    Network.asteroid.getAllAsteroidsAsync(startDate, endDate)
                val jsonObject = JSONObject(response)
                asteroidList = parseAsteroidsJsonResult(jsonObject)
                database.asteroidDao().deletePreviousDay(today())
                database.asteroidDao().insertAll(*asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.d(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

    // Get picture of day from network and check if it's photo or video and save the image to roomdb
    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = Network.asteroid.getPictureOfDayAsync().await()
                if (pictureOfDay.mediaType == "image") {
                    database.pictureOfDayDao().insertImage(pictureOfDay.asDatabaseModel())
                } else return@withContext
            } catch (e: Exception) {
                Log.d(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

}