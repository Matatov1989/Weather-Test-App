package com.example.weathertest.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data") val weatherData: WeatherData
)
