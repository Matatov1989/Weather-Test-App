package com.example.weathertest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weathertest.model.LastWeatherData
import com.example.weathertest.model.WeatherData
import kotlinx.coroutines.flow.Flow

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


//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertService(vararg serviceModel: LastWeatherData)
}