package com.example.weathertest.network

import com.example.weathertest.model.WeatherResponse
import com.example.weathertest.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather/latest/by-lat-lng")
    suspend fun getWeather(
        @Query("lat") latitude: Double = 32.1602438,
        @Query("lng") longitude: Double = 34.8095785,
        @Header("x-api-key") apiKey: String = Constants.WEATHER_API
    ): Response<WeatherResponse>
}