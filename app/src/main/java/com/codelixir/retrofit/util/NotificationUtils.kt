package com.codelixir.retrofit.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.codelixir.retrofit.R
import androidx.core.app.NotificationCompat

internal object NotificationUtils {
    fun sendNotification(
        context: Context,
        title: String,
        text: String,
        notificationId: Int = 0,
        notificationChannel: String? = null
    ) {
        val channelName = notificationChannel ?: context.getString(R.string.app_name)
        val channelId = channelName.removeWhitespaces()
        val builder = NotificationCompat.Builder(context, channelName)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        builder.setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setColorized(true)
            .setColor(context.getColor(R.color.colorAccent))
            .setLights(Color.RED, 3000, 3000)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    priority = NotificationManager.IMPORTANCE_HIGH
                }
            }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel(
                        channelId, channelName, NotificationManager.IMPORTANCE_HIGH
                    ).also { channel ->
                        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                        mNotificationManager.createNotificationChannel(channel)
                    }
                    setChannelId(channelId)
                }
            }

        // Issue the notification
        mNotificationManager.notify(notificationId, builder.build())
    }
}