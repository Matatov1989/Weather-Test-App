package com.example.weathertest.fragments.map

import androidx.lifecycle.ViewModel
import com.example.weathertest.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {
}