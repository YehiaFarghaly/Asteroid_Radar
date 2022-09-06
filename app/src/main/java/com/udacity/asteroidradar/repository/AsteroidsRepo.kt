package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRoom
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.await

class AsteroidsRepo(private val database: AsteroidRoom) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.toMutableListOfAsteroids()
        }
    val image: LiveData<PictureOfDay> = database.pictureDao.get()

    val asteroidsToday: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getTodayAsteroid(
            Today()
        )
    ) {
        it.toMutableListOfAsteroids()
    }
    val asteroidsThisWeek: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getThisWeekAsteroid(
            Today(), afterWeek()
        )
    ) {
        it.toMutableListOfAsteroids()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidResponse = AsteroidsAPI.retrofitService.getAsteroids(Constants.API_KEY)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponse))
            Log.i("yehia", asteroidList[0].codename)
            database.asteroidDao.insertAll(*asteroidList.toArrOfDatabase())
            Log.i("yehia3", asteroids.value.toString())
        }
    }

    suspend fun refreshPicture() {
        Log.i("yehiaImage", "Entered")
        withContext(Dispatchers.IO) {
            val imageResponse = AsteroidsAPI.retrofitService.getImageOfTheDay(Constants.API_KEY)
            Log.i("yehiaImage", imageResponse.title)
            database.pictureDao.insert(imageResponse)
            Log.i("yehiaaa",image.value!!.title)
        }

    }

    suspend fun deleteAsteroid() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroid(Today())
        }
    }
}