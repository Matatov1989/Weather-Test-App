package com.example.weathertest.data

import com.example.weathertest.model.WeatherData

sealed class WeatherUiState() {
    data class Success(val weatherData: WeatherData?) : WeatherUiState()
    data class Error(val exception: Exception) : WeatherUiState()
    data class Loading(val isLoading: Boolean) : WeatherUiState()
    data class AvailableInternet(val isShowMessage: Boolean) : WeatherUiState()
}
