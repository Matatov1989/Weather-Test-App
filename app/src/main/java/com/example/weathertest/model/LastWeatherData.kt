package com.example.weathertest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherTable")
data class LastWeatherData(
    @PrimaryKey
    val time: Long,
    val weatherData: String
)
