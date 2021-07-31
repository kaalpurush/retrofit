package com.codelixir.retrofit.service

import android.app.PendingIntent
import android.os.Build
import android.service.controls.Control
import android.service.controls.DeviceTypes
import android.service.controls.templates.RangeTemplate
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
data class MyControlDevice(
     val deviceId: String,
     val deviceName: String,
     val room: String?,
    var openState: Float?
)  {

     fun toStatelessControl(pendingIntent: PendingIntent): Control =
        Control.StatelessBuilder(deviceId, pendingIntent)
            .setTitle(deviceName)
            .setSubtitle(room ?: "Unknown Room")
            .setDeviceType(DeviceTypes.TYPE_BLINDS)
            .build()

     fun toStatefulControl(pendingIntent: PendingIntent): Control {
        val rangeTemplate = RangeTemplate(
            "${deviceId}-range",
            0f,
            1f,
            openState ?: 0f,
            0.01f,
            null
        )

        return Control.StatefulBuilder(deviceId, pendingIntent)
            .setTitle(deviceName)
            .setSubtitle(room ?: "Unknown Room")
            .setDeviceType(DeviceTypes.TYPE_BLINDS)
            .setStatus(Control.STATUS_OK)
            .setControlTemplate(rangeTemplate)
            .build()
    }
}