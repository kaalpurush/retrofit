package com.codelixir.retrofit

import android.content.Context

class Application : android.app.Application() {

    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}
