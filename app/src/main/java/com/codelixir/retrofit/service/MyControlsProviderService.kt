package com.codelixir.retrofit.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.ControlAction
import android.service.controls.templates.ControlButton
import android.service.controls.templates.StatelessTemplate
import android.service.controls.templates.ToggleTemplate
import android.util.Log
import androidx.annotation.RequiresApi
import com.codelixir.retrofit.R
import com.codelixir.retrofit.ui.MainActivity
import com.codelixir.retrofit.util.toast
import java.util.concurrent.Flow
import java.util.function.Consumer


@RequiresApi(Build.VERSION_CODES.R)
class MyControlsProviderService : ControlsProviderService() {
    val stateMap = mutableMapOf<String, String>()
    var subscriberUpdate: Flow.Subscriber<in Control>? = null

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return Flow.Publisher { subscriber ->
            val pendingIntent = PendingIntent.getActivity(
                baseContext,
                1,
                Intent(baseContext, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            for (i in 1..3) {
                val control = Control.StatelessBuilder("Device$i", pendingIntent)
                    .setTitle("Device$i")
                    .setSubtitle("Unknown Room")
                    .setCustomIcon(Icon.createWithResource(baseContext, R.drawable.ic_back))
                    .setDeviceType(DeviceTypes.TYPE_GENERIC_ON_OFF)
                    .build()

                subscriber.onNext(control)
            }

            subscriber.onComplete()
        }
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
/*        return flow {
            for (controlId in controlIds) {
                val pendingIntent = PendingIntent.getActivity(
                    baseContext,
                    1,
                    Intent(baseContext, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val control = Control.StatefulBuilder(controlId, pendingIntent)
                    .setTitle(controlId)
                    .setSubtitle("Unknown Room")
                    .setCustomIcon(Icon.createWithResource(baseContext, R.drawable.ic_back))
                    .setDeviceType(DeviceTypes.TYPE_GENERIC_ON_OFF)
                    .setStatus(Control.STATUS_OK)
                    .setStatusText(state)
                    .setControlTemplate(
                        StatelessTemplate(
                            controlId
                        )
                    )
                    .build()
                emit(control)
            }
        }.asPublisher()*/

        return Flow.Publisher { subscriber ->
            val pendingIntent = PendingIntent.getActivity(
                baseContext,
                1,
                Intent(baseContext, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            subscriberUpdate = subscriber

            subscriber.onSubscribe(object : Flow.Subscription {
                override fun request(n: Long) {
                    for (controlId in controlIds) {
                        Log.d("onSubscribe", n.toString())
                        val control = Control.StatefulBuilder(controlId, pendingIntent)
                            .setTitle(controlId)
                            .setSubtitle("Unknown Room")
                            .setCustomIcon(Icon.createWithResource(baseContext, R.drawable.ic_back))
                            .setDeviceType(DeviceTypes.TYPE_GENERIC_ON_OFF)
                            .setStatus(Control.STATUS_OK)
                            .setStatusText(stateMap.getOrDefault(controlId, "Off"))
                            .setControlTemplate(
                                StatelessTemplate(
                                    controlId
                                )
                            )
                            .build()
                        subscriber.onNext(control)
                        stateMap[controlId] = stateMap.getOrDefault(controlId, "Off")
                    }
                }

                override fun cancel() {

                }
            })
        }
    }

    override fun performControlAction(
        controlId: String,
        action: ControlAction,
        consumer: Consumer<Int>
    ) {
        val state = if (stateMap.getOrDefault(controlId, "Off") == "On") "Off" else "On"
        stateMap[controlId] = state

        val pendingIntent = PendingIntent.getActivity(
            baseContext,
            1,
            Intent(baseContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val control = Control.StatefulBuilder(controlId, pendingIntent)
            .setTitle(controlId)
            .setSubtitle("Unknown Room")
            .setCustomIcon(Icon.createWithResource(baseContext, R.drawable.ic_back))
            .setDeviceType(DeviceTypes.TYPE_GENERIC_ON_OFF)
            .setStatus(Control.STATUS_OK)
            .setStatusText(state)
            .setControlTemplate(
                StatelessTemplate(
                    controlId
                )
            )
            .build()

        subscriberUpdate?.onNext(control)

        consumer.accept(ControlAction.RESPONSE_OK)
        toast(
            applicationContext,
            "performControlAction($controlId, ${action.actionType}, ${action.templateId}, $consumer)"
        )
    }
}