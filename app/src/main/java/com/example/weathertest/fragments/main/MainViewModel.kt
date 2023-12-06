package com.example.weathertest.fragments.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertest.WeatherApplication
import com.example.weathertest.data.WeatherUiState
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

    init {
        val isAvailableInternet = WeatherApplication().isAvailableInternet
        if (isAvailableInternet) {
            getWeather()
        } else {
            weatherUiState.value = WeatherUiState.AvailableInternet(true)

           // TODO : get from local data
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            try {
                weatherUiState.value = WeatherUiState.Loading(true)
                val response = repository.getWeather()
                val data = response.body()?.weatherData

                data?.let {
                    weatherUiState.value = WeatherUiState.Success(it)
                }
            } catch (e: Exception) {
                Log.e("RESULT_EXCEPTION", "result: $e")
                weatherUiState.value = WeatherUiState.Error(e)
            } finally {
                weatherUiState.value = WeatherUiState.Loading(false)
            }
        }
    }

}