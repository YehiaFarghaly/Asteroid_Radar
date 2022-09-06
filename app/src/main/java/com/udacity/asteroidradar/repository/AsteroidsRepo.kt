package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(private val database: AsteroidRoom) {
    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroids()
    val image=MutableLiveData<PictureOfDay>()

    val asteroidsToday: LiveData<List<Asteroid>> = database.asteroidDao.getTodayAsteroid(Today())
    val asteroidsThisWeek: LiveData<List<Asteroid>> = database.asteroidDao.getThisWeekAsteroid(Today(),
        afterWeek()
    )

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidResponse = AsteroidsAPI.retrofitService.getAsteroids(Constants.API_KEY)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
            Log.i("yehia", asteroidList[0].codename)
            database.asteroidDao.deleteAsteroid()
            database.asteroidDao.insertAll(asteroidList)
            Log.i("yehia3", asteroids.value.toString())
        }
    }

    suspend fun refreshPicture() {
     val pictureOfDay = withContext(Dispatchers.IO) {
         return@withContext AsteroidsAPI.retrofitService.getImageOfTheDay(Constants.API_KEY)
     }
        pictureOfDay.let {
    image.value = it
        }
    }
    fun getPicture(url:String,title:String) {
    image.value= PictureOfDay("image",title,url)
    }

    suspend fun deleteAsteroid() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroid()
        }
    }
}