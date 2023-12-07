package com.example.weathertest

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weathertest.helpers.ConnectivityHelper
import com.example.weathertest.services.GPSWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class WeatherApplication : Application() {

    companion object {
        var isAvailableInternet: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        checkAvailableInternet()

        if (checkPermissionLocation()) {
            initWorkerGPSTenSeconds()
            initWorkerGPSDaily()
        }
    }

    private fun checkAvailableInternet() {
        isAvailableInternet = ConnectivityHelper(baseContext).isInternetAvailable()
    }

    private fun initWorkerGPSTenSeconds() {
        val constraintsForeground = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequestForeground = PeriodicWorkRequestBuilder<GPSWorker>(
            repeatInterval = 10,    // 10 seconds
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        )
            .setConstraints(constraintsForeground)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequestForeground)
    }

    private fun initWorkerGPSDaily() {
        val constraintsDaily = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val periodicWorkRequestDaily = PeriodicWorkRequestBuilder<GPSWorker>(
            repeatInterval = 1, // 1 day
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraintsDaily)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequestDaily)
    }

    private fun checkPermissionLocation(): Boolean {
        return ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }
}