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
import java.text.SimpleDateFormat
import java.util.*


class NasaRepository(private val database: AsteroidDatabase) {

    private var today = ""

    private val _imageOfDay = MutableLiveData<ImageOfDay>()

    val imageOfDay: LiveData<ImageOfDay>
        get() = _imageOfDay

    init {
        val calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd");
        today = df.format(calendar.time)
    }


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(today)) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids(startDate: String, endDate: String = "") {
        withContext(Dispatchers.IO) {
            val asteroidList = Network.nasaService.getAsteroidlist(startDate, endDate).await()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroidList)).asDatabaseModel())
        }
    }

    suspend fun getImageOfDay() {
        withContext(Dispatchers.IO) {
            val image = Network.nasaService.getImageOfDay().await()
            _imageOfDay.postValue(image.asDomainModel())
        }
    }
}
