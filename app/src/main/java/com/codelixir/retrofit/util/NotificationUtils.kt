package com.codelixir.retrofit.util;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.codelixir.retrofit.R

internal object NotificationUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(context: Context, title: String?, text: String?) {
        val channelName = context.getString(R.string.app_name)
        val channelId = channelName.removeWhitespaces()
        val builder: Notification.Builder = Notification.Builder(context, channelName)
        builder.setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setColor(context.resources.getColor(R.color.colorAccent))
            .setLights(Color.RED, 3000, 3000)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setColorized(true)
                }
            }

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mNotificationManager.createNotificationChannel(mChannel)
            builder.setChannelId(channelId)
        }

        // Issue the notification
        mNotificationManager.notify(0, builder.build())
    }
}