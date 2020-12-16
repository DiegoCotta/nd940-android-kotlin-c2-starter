package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroid order by closeApproachDate")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where closeApproachDate = :date order by closeApproachDate")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where closeApproachDate BETWEEN :dateStart AND :dateEnd order by closeApproachDate")
    fun getWeekAsteroids(dateStart: String, dateEnd: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: AsteroidEntity)

    @Query("DELETE FROM asteroid WHERE closeApproachDate < :date")
    fun deleteOldAsteroids(date: String)
}

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid_database"
            ).build()
        }
    }
    return INSTANCE
}
