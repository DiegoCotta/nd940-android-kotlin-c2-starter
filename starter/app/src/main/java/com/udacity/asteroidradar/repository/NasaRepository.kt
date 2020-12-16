package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.ImageOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class NasaRepository(private val database: AsteroidDatabase) {

    private val _imageOfDay = MutableLiveData<ImageOfDay>()

    val imageOfDay: LiveData<ImageOfDay>
        get() = _imageOfDay


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getTodayAsteroids(
            getTodayDate()
        )
    ) {
        it.asDomainModel()
    }

    val weekAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getWeekAsteroids(
            getTodayDate(),
            getLastDayOfWeek()
        )
    ) {
        it.asDomainModel()
    }


    suspend fun refreshAsteroids(startDate: String ) {
        withContext(Dispatchers.IO) {
            val asteroidList = Network.nasaService.getAsteroidlist(startDate).await()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroidList)).asDatabaseModel())
        }
    }


    suspend fun deleteOldAsteroid(date: String) {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteOldAsteroids(date)
        }
    }


    suspend fun getImageOfDay() {
        withContext(Dispatchers.IO) {
            val image = Network.nasaService.getImageOfDay().await()
            _imageOfDay.postValue(image.asDomainModel())
        }
    }
}
