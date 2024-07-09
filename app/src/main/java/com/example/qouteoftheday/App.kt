package com.example.qouteoftheday

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.qouteoftheday.workers.DailyQuoteWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class QuotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(DailyQuoteWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                getString(R.string.daily_notif_tag),
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

}