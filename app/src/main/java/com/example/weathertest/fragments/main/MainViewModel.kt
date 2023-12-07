package com.example.weathertest.fragments.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertest.WeatherApplication
import com.example.weathertest.data.WeatherUiState
import com.example.weathertest.model.WeatherData
import com.example.weathertest.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val weatherUiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Success(null))
    val weatherLiveData: StateFlow<WeatherUiState> = weatherUiState


    fun getWeather(location: Location) {
        val isAvailableInternet = WeatherApplication.isAvailableInternet

        if (isAvailableInternet) {
            getWeatherFromApi(location)
        } else {
            weatherUiState.value = WeatherUiState.AvailableInternet(true)
            getWeatherFromCache()
        }
    }

    private fun getWeatherFromApi(location: Location) {
        viewModelScope.launch {
            try {
                weatherUiState.value = WeatherUiState.Loading(true)
                val response = repository.getWeather(location)
                val data = response.body()?.weatherData

                data?.let {
                    weatherUiState.value = WeatherUiState.Success(it)
                    cacheWeather(it)
                }
            } catch (e: Exception) {
                Log.e("RESULT_EXCEPTION", "result: $e")
                weatherUiState.value = WeatherUiState.Error(e)
            } finally {
                weatherUiState.value = WeatherUiState.Loading(false)
            }
        }
    }

    private fun cacheWeather(weather: WeatherData) {
        viewModelScope.launch {
            if (repository.isEmpty() == 0) {
                repository.cacheWeather(weather)
            } else {
                repository.updateCache(weather)
            }
        }
    }

    private fun getWeatherFromCache() {
        viewModelScope.launch {
            if (repository.isEmpty() != 0) {
                val response = repository.getWeatherFromCache()
                weatherUiState.value = WeatherUiState.Success(response)
            }
        }
    }
}