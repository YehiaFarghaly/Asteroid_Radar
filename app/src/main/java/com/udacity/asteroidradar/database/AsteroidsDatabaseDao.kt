package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidsDatabaseDao {
    @Query("select * from asteroids_table order by closeApproachDate asc")
    fun getAsteroids():  LiveData<List<AsteroidsDatabase>>
    @Query("select * from asteroids_table where closeApproachDate = :today order by closeApproachDate asc")
    fun getTodayAsteroid(today:String):LiveData<List<AsteroidsDatabase>>
    @Query("select * from asteroids_table where closeApproachDate between :start and :end order by closeApproachDate asc")
    fun getThisWeekAsteroid(start:String,end:String):LiveData<List<AsteroidsDatabase>>
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidsDatabase)
    @Query("delete from asteroids_table where closeApproachDate<:day")
    fun deleteAsteroid(day:String):Int
}
@Database(entities = [AsteroidsDatabase::class,PictureOfDay::class], version=2)
abstract class AsteroidRoom:RoomDatabase() {
    abstract val asteroidDao:AsteroidsDatabaseDao
    abstract val pictureDao:PictureOfTheDayDao
}
private lateinit var INSTANCE:AsteroidRoom
fun getDatabase(context:Context):AsteroidRoom {
    synchronized(AsteroidRoom::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRoom::class.java,
                "asteroids_database"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}
