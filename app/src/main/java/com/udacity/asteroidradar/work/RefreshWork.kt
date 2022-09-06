package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import retrofit2.HttpException

class RefreshWork(appContext: Context, param:WorkerParameters):
    CoroutineWorker(appContext,param) {
    companion object {
        const val WORK_NAME= "RefreshWorker"
    }
    override suspend fun doWork(): Result {
        val dataBase = getDatabase(applicationContext)
        val repo = AsteroidsRepo(dataBase)
        return try {
            repo.refreshAsteroids()
            repo.refreshPicture()
            Result.success()
        }
        catch (HttpException:HttpException) {
            Result.retry()
        }
    }
}