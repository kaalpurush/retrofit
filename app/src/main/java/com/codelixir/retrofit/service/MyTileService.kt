package com.codelixir.retrofit.service

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.work.*
import com.codelixir.retrofit.R
import com.codelixir.retrofit.ui.MainActivity
import com.codelixir.retrofit.util.getClass
import com.codelixir.retrofit.util.toast
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.N)
class MyTileService : TileService() {
    var count = 0

    override fun onCreate() {
        requestListeningState(this, ComponentName(this, javaClass))
        schedulePeriodicUpdate()
        super.onCreate()
    }

    private fun schedulePeriodicUpdate() {
        val workBuilder = PeriodicWorkRequest.Builder(
            TileWorker::class.java, 15,
            TimeUnit.MINUTES
        ).addTag(TileWorker::getClass.name)

        val work = workBuilder.build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                TileWorker::getClass.name,
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }

    override fun onStartListening() {
        updateTileResources()
        super.onStartListening()
    }

    override fun onTileRemoved() {
        WorkManager.getInstance(this).cancelAllWorkByTag(TileWorker::getClass.name)
        super.onTileRemoved()
    }

    override fun onClick() {
        showDialog()
        super.onClick()
    }

    private fun updateTileResources() {
        toast(this, "Updating: ${++count}")
        if (this.qsTile != null) {
            val tile = this.qsTile
            tile.label = getString(R.string.app_name)

            tile.icon = Icon.createWithResource(
                applicationContext,
                R.drawable.ic_back
            )
            tile.state =
                if (tile.state == Tile.STATE_ACTIVE) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
            tile.updateTile()
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        builder.setCancelable(true)
            .setIcon(R.drawable.ic_back)
            .setTitle(R.string.app_name)
            .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }

        builder.setMessage(R.string.app_name)
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
/*            startActivityAndCollapse(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )*/
        }
        showDialog(builder.create())
    }
}