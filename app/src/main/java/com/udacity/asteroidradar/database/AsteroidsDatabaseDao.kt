package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidsDatabaseDao {
    @Query("select * from asteroid_table order by closeApproachDate asc")
    fun getAsteroids():  LiveData<List<Asteroid>>
    @Query("select * from asteroid_table where closeApproachDate = :today order by closeApproachDate asc")
    fun getTodayAsteroid(today:String):LiveData<List<Asteroid>>
    @Query("select * from asteroid_table where closeApproachDate between :start and :end order by closeApproachDate asc")
    fun getThisWeekAsteroid(start:String,end:String):LiveData<List<Asteroid>>
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: ArrayList<Asteroid>)
    @Query("delete from asteroid_table")
    fun deleteAsteroid()
}
@Database(entities = [Asteroid::class], version=1)
abstract class AsteroidRoom:RoomDatabase() {
    abstract val asteroidDao:AsteroidsDatabaseDao

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
