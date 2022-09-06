package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Clicked
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidsRepo(database)
     var clickedState = MutableLiveData(Clicked.All)
    private var cache:SharedPreferences=application.getSharedPreferences("ImageCache", Context.MODE_PRIVATE)
    private var picCache:SharedPreferences.Editor=cache.edit()
    private val _selectAsteroid = MutableLiveData<Asteroid>()
    val selectAsteroid:LiveData<Asteroid>
    get()=_selectAsteroid
    val asteroid = asteroidRepo.asteroids
    val asteroidToday = asteroidRepo.asteroidsToday
    val asteroidThisWeek = asteroidRepo.asteroidsThisWeek
    val pictureOfDay = asteroidRepo.image
init {
    Log.i("yehia","viewModel called")
    viewModelScope.launch {
        try {
            Log.i("yehia1","asteroid repo refreshed")
            asteroidRepo.refreshAsteroids()
            Log.i("yehia2",asteroid.toString())
        }
        catch (e:java.lang.Exception) {
            Log.i("yehia","asteroid failed")
        }
        try {
            Log.i("yehia1","pictures refreshed")
            asteroidRepo.refreshPicture()
            asteroidRepo.image.value.let { getCachePicture(it!!) }
        }
        catch (e:Exception) {
            cache.getString("url","")?.let {
                asteroidRepo.getPicture(it,
                    cache.getString("title","")!!
                )
            }
        }
    }
}
    private fun getCachePicture(pictureOfDay: PictureOfDay) {
        picCache.putString("title",pictureOfDay.title)
        picCache.putString("url",pictureOfDay.url)
        picCache.apply()
    }


}