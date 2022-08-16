package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BaseApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    //once the app lunched the onCreate will execute
    override fun onCreate() {
        super.onCreate()
        workerCall()
    }

    private fun workerCall() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {

        // Set options of work manager
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        // Set repeating request and pass the constraints
        val repeatingRequest =
            PeriodicWorkRequestBuilder<AsteroidWorker>(1, TimeUnit.DAYS).setConstraints(constraints)
                .build()

        // create instance from the workManager
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "Asteroid Work",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}