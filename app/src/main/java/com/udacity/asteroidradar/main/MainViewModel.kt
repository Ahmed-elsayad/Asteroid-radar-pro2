package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.MarsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.roomdb.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(
    private val dataBase: AsteroidDao,
    private val imageDb: ImageDao,
    application: Application,
) : AndroidViewModel(application){

    @RequiresApi(Build.VERSION_CODES.O)
    private val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)
    lateinit var getListBack: List<Asteroid>
    lateinit var getImageBack: PictureOfDay
    var getParsedAsteroids = ArrayList<Asteroid>()

    val _parseData = MutableLiveData<List<Asteroid>>()
    val parseData: LiveData<List<Asteroid>>
        get() = _parseData

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
            getAsteroidsUpdate()
            getAsteroidFromDatabase()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getAsteroidsUpdate() {
        try {
            val listResult = MarsApi.retrofitService.getAsteroids(startDate, currentDate)
            val pictureOfDay = MarsApi.retrofitService.getImageOfTheDay()
            val jsonAsteroids = JSONObject(listResult)
            getParsedAsteroids = parseAsteroidsJsonResult(jsonAsteroids)
            val networkAsteroidList = getParsedAsteroids.map {
                Asteroid(
                    it.id,
                    it.codename,
                    it.closeApproachDate,
                    it.absoluteMagnitude,
                    it.estimatedDiameter,
                    it.relativeVelocity,
                    it.distanceFromEarth,
                    it.isPotentiallyHazardous
                )
            }
            insertAll(networkAsteroidList)
            insertPictureInDatabase(pictureOfDay)
            getPictureFromDatabase()
        } catch (e: Exception) {
            Toast.makeText(getApplication(), "Error Loading", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getPictureFromDatabase() {
        withContext(Dispatchers.IO) {
            val gettingImage = imageDb.getPictureOfDay()
            getImageBack = gettingImage.asDomainModelPicture()
        }
        _pictureOfDay.value = getImageBack
    }

    private suspend fun getAsteroidFromDatabase() {
        withContext(Dispatchers.IO) {
            val gettingAsteroids = dataBase.getAllSavedAsteroids()
            getListBack = gettingAsteroids.asDomainModel()
        }
        _parseData.value = getListBack
    }

    suspend fun insertPictureInDatabase(pictureOfDayEntity: PictureOfDayEntity) {
        withContext(Dispatchers.IO) {
            imageDb.insertPOD(pictureOfDayEntity)
        }
    }

    private suspend fun insertAll(networkAsteroidList: List<Asteroid>) {
        withContext(Dispatchers.IO) {
            dataBase.insertAll(*networkAsteroidList.asDatabaseModel())
        }
    }

}
