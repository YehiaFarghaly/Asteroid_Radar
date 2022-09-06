package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids_table")
data class AsteroidsDatabase (
    @PrimaryKey
    val id:Long,
    val codeName:String,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val isPotentiallyHazardous: Boolean,
    val distanceFromEarth: Double,
    )