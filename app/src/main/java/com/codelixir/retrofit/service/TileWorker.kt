package com.codelixir.retrofit.service

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.service.quicksettings.TileService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

@TargetApi(Build.VERSION_CODES.N)
class TileWorker(var context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            TileService.requestListeningState(
                context, ComponentName(context, MyTileService::class.java)
            )
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}