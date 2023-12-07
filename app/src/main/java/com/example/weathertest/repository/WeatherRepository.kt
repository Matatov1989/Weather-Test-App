package com.example.weathertest.repository

import android.location.Location
import com.example.weathertest.data.WeatherDao
import com.example.weathertest.model.WeatherData
import com.example.weathertest.model.WeatherResponse
import com.example.weathertest.network.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi, private val weatherDao: WeatherDao) {
    suspend fun getWeather(location: Location): Response<WeatherResponse> = api.getWeather(location.latitude, location.longitude)
    suspend fun cacheWeather(weather: WeatherData) = weatherDao.insert(weather)
    suspend fun isEmpty(): Int = weatherDao.isEmpty()
    suspend fun getWeatherFromCache(): WeatherData = weatherDao.getWeatherFromCache()
    suspend fun updateCache(weather: WeatherData) = weatherDao.updateWeather(weather)
    suspend fun isEmptyWeatherByTimezone(timeZone: String): Int = weatherDao.isEmptyWeatherByTimezone(timeZone)
}