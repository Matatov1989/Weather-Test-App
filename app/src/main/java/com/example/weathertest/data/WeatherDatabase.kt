package com.example.weathertest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weathertest.model.WeatherData

@Database(entities = [WeatherData::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}