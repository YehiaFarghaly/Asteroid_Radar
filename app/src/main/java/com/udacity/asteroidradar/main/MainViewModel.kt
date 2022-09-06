package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Clicked
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidsAPI
import com.udacity.asteroidradar.api.toMutableListOfAsteroids
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidsRepo(database)
    private var clickedState = MutableLiveData(Clicked.All)
    private val _asteroidsList =

        Transformations.switchMap(clickedState) {
            when(it) {
                Clicked.All ->asteroidRepo.asteroids
                Clicked.ThisWeek -> asteroidRepo.asteroidsThisWeek
                else -> asteroidRepo.asteroidsToday
            }
        }
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList
    private val _picOfTheDay = Transformations.switchMap(MutableLiveData
        (asteroidRepo.image)) {
        it
    }
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _picOfTheDay

   fun onItemClicked(item:Clicked) {
       clickedState.postValue(item)
   }
    init {
                viewModelScope.launch {
            asteroidRepo.refreshAsteroids()
            asteroidRepo.refreshPicture()
                    Log.i("yehiado",_picOfTheDay.value.toString())
        }


    }


}