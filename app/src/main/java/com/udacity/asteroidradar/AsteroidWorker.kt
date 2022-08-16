package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.sevenDaysAfter
import com.udacity.asteroidradar.api.today
import com.udacity.asteroidradar.roomdb.getDatabase
import retrofit2.HttpException

class AsteroidWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        /* Refresh the data from network and save it to database, also refreshes the picture of
         the day from network and save it room db*/
        return try {
            repository.refreshAsteroids(today(), sevenDaysAfter())
            repository.refreshPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}