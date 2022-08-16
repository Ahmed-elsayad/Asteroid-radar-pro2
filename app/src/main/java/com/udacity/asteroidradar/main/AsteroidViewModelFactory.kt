package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.roomdb.AsteroidDao
import com.udacity.asteroidradar.roomdb.ImageDao

class AsteroidViewModelFactory(private val dataSource: AsteroidDao,
                               private val imageDao: ImageDao,
                               private val application: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                dataSource,
                imageDao,
                application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}