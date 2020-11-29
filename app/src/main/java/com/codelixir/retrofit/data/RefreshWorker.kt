package com.codelixir.retrofit.data

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.codelixir.retrofit.Application
import com.codelixir.retrofit.R
import androidx.core.app.NotificationCompat.Builder


class RefreshWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        try {
            setForeground(createForegroundInfo())

            Application.getSetting("worker", 0).also {
                Application.saveSetting("worker", it + 1)
            }

            val data = GitHubDataRepository.fetchRepositories()
            if (data != null) {
                GitHubDataRepository.insertRepositories(data)
                return Result.success()
            } else {
                return Result.failure()
            }
        } catch (throwable: Throwable) {
            return Result.failure()
        }
    }

    /**
     * Create ForegroundInfo required to run a Worker in a foreground service.
     */
    private fun createForegroundInfo(): ForegroundInfo {
        // For a real world app you might want to use a different id for each Notification.
        val notificationId = 1
        return ForegroundInfo(notificationId, createNotification())
    }

    /**
     * Create the notification and required channel (O+) for running work in a foreground service.
     */
    private fun createNotification(): Notification {
        val context = applicationContext
        val channelId = context.getString(R.string.notification_channel_id)
        val title = context.getString(R.string.app_name)
        val cancel = context.getString(android.R.string.cancel)
        val name = context.getString(R.string.notification_channel_name)
        // This PendingIntent can be used to cancel the Worker.
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        val builder = Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setTicker(title)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, name).also {
                builder.setChannelId(channelId)
            }
        }
        return builder.build()
    }

    /**
     * Create the required notification channel for O+ devices.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            channelId, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }
}