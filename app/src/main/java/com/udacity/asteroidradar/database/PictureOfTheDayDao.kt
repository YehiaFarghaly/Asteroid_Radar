package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface PictureOfTheDayDao {
@Query("select * from pic_of_day")
fun get():LiveData<PictureOfDay>
@Insert(onConflict = OnConflictStrategy.REPLACE)
fun insert(img:PictureOfDay):Long
}