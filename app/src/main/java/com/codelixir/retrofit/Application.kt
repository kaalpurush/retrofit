package com.codelixir.retrofit

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.work.*
import com.codelixir.retrofit.service.RefreshWorker
import com.codelixir.retrofit.util.getClass
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class Application : android.app.Application(), Configuration.Provider {

    companion object {
        lateinit var context: Context
            private set

        fun getSetting(name: String, default_value: Int): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(name, default_value)
        }

        fun saveSetting(name: String, value: Int) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(name, value)
                .apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        setupRecurringWork()
    }

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.VERBOSE)
            .build()

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.DAYS)
            .addTag("RefreshWorker")
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                RefreshWorker::getClass.name,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
    }
}
