package com.example.weathertest.services

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters


class GPSWorker (context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val intent = Intent(applicationContext, GPSService::class.java)

        applicationContext.startService(intent)

        return Result.success()
    }
}