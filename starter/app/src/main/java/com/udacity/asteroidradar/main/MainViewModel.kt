package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {



    private val _listAsteroids = MutableLiveData<List<Asteroid>>()

    val listAsteroids: LiveData<List<Asteroid>>
        get() = _listAsteroids


    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()

    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    fun displayAsteroidDetails(marsProperty: Asteroid) {
        _navigateToAsteroidDetails.value = marsProperty
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }
}