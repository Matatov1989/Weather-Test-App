package com.example.weathertest.repository

import com.example.weathertest.model.WeatherResponse
import com.example.weathertest.network.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {


    suspend fun getWeather(): Response<WeatherResponse> = api.getWeather()

}