package com.example.weathertest

import android.app.Application
import com.example.weathertest.helpers.ConnectivityHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication : Application() {

    var isAvailableInternet: Boolean = false

    override fun onCreate() {
        super.onCreate()

        checkAvailableInternet()
    }

    private fun checkAvailableInternet() {
        isAvailableInternet = ConnectivityHelper(baseContext).isInternetAvailable()
    }
}