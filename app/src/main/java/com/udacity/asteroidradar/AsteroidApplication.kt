package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication:Application() {
    private val appScope = CoroutineScope(
  Dispatchers.Default
    )
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
    private fun delayedInit() {
        appScope.launch {
            setupWork()
        }
    }
    private fun setupWork() {
        val constrains = Constraints.Builder().
        setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true).apply {
 if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
     setRequiresDeviceIdle(true)
 }
            }.build()
val request = PeriodicWorkRequestBuilder<RefreshWork>(
    1,TimeUnit.DAYS
).setConstraints(constrains)
    .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}