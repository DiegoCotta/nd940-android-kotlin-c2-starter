package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getLastDayOfWeek
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val nasaRepository = NasaRepository(database)

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()

    val navigateToAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetails

    fun displayAsteroidDetails(marsProperty: Asteroid) {
        _navigateToAsteroidDetails.value = marsProperty
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }

    private val menuFilter = MutableLiveData<MenuFilter>()

    init {
        menuFilter.value = MenuFilter.TODAY
        viewModelScope.launch {

            try {
                nasaRepository.refreshAsteroids(getTodayDate())
            } catch (e: Exception) {

            }
        }
        viewModelScope.launch {
            try {
                nasaRepository.getImageOfDay()
            } catch (e: Exception) {

            }
        }
    }

    val imageOfDay = nasaRepository.imageOfDay
    val asteroidList = Transformations.switchMap(menuFilter) { filter ->
        return@switchMap when (filter) {
            MenuFilter.TODAY -> nasaRepository.todayAsteroids
            MenuFilter.WEEK -> nasaRepository.weekAsteroids
            MenuFilter.ALL -> nasaRepository.asteroids
        }
    }

    fun setFilterAll() {
        menuFilter.value = MenuFilter.ALL
    }

    fun setFilterWeek() {
        menuFilter.value = MenuFilter.WEEK

    }

    fun setFilterToday() {
        menuFilter.value = MenuFilter.TODAY
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
