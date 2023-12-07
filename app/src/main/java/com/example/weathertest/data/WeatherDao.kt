package com.example.weathertest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weathertest.model.WeatherData

@Dao
interface WeatherDao {
    @Insert
    suspend fun insert(weather: WeatherData)

    @Query("SELECT COUNT() FROM WeatherTable")
    suspend fun isEmpty(): Int

    @Query("SELECT * FROM WeatherTable")
    suspend fun getWeatherFromCache(): WeatherData

    @Update
    suspend fun updateWeather(weather: WeatherData)

    @Query("SELECT COUNT() FROM WeatherTable WHERE timezone < :timeZone")
    suspend fun isEmptyWeatherByTimezone(timeZone: String): Int
}