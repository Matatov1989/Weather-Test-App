package com.example.weathertest.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "WeatherTable")
data class WeatherData(
    @PrimaryKey
    val time: Long,
    val temperature: Double,
    val timezone: String,
    val summary: String,
    val icon: String
)

