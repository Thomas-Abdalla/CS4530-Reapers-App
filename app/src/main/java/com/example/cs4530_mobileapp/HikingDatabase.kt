package com.example.cs4530_mobileapp

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [WeatherTable::class, UserTable::class], version = 1, exportSchema = false)
abstract class HikingDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun userDao(): UserDao

    // Make the db singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        @Volatile
        private var mInstance: HikingDatabase? = null
        fun getDatabase(
            context: Context,
            scope : CoroutineScope
        ): HikingDatabase {
            return mInstance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HikingDatabase::class.java, "hiking.db"
                )
                    .addCallback(RoomDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                mInstance = instance
                instance
            }
        }

        private class RoomDatabaseCallback(
                private val scope: CoroutineScope
        ): Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                mInstance?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateWeatherDbTask(database.weatherDao())
                        populateUserDbTask(database.userDao())
                    }
                }
            }
        }

        suspend fun populateWeatherDbTask (weatherDao: WeatherDao) {
           weatherDao.insert(WeatherTable("Dummy_loc","Dummy_data"))
        }
        suspend fun populateUserDbTask (userDao: UserDao) {
            userDao.insert(UserTable(0,"John","Doe", 18, 70, 180, 0.0f, 0, 0, 2))
        }
    }
}