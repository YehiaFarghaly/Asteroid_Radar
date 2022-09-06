package com.udacity.asteroidradar.api

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AsteroidsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") api_key:String
    ) :String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(
        @Query("api_key") api_key:String
    ):PictureOfDay
}
object AsteroidsAPI {

    private val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private fun getHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL).client(getHttpClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
 val retrofitService:AsteroidsApiService by lazy {
     Log.i("yehia","created")
     retrofit.create(AsteroidsApiService::class.java)
 }
}