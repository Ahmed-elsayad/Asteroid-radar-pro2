package com.udacity.asteroidradar.roomdb

// Room step 2 add entities
// Room step3 add abstract val
// Change version number if schema updated

import android.content.Context
import androidx.room.*


@Database(
    entities = [
        AsteroidEntity::class,
        PictureOfDayEntity::class],
    version = 1,
)
abstract class AsteroidDb : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val imageDao: ImageDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDb? = null

        fun getInstance(context: Context): AsteroidDb {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDb::class.java,
                    "AsteroidDb"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}
