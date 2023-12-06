package com.example.weathertest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathertest.model.LastWeatherData

@Dao
interface WeatherDao {


    @Query("SELECT * FROM WeatherTable")
    suspend fun getServices(): LastWeatherData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(vararg serviceModel: LastWeatherData)
}