package com.codelixir.retrofit

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {
    private val client by lazy {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB

        val cache = Cache(Application.context!!.getCacheDir(), cacheSize)

        val okHttpClient = OkHttpClient.Builder().run {
            //cache(cache)
            build()
        }

        Retrofit.Builder().run {
            client(okHttpClient)
            baseUrl("https://api.github.com/")
            addConverterFactory(MoshiConverterFactory.create())
            build()
        }
    }

    fun get(): Retrofit {
        return client
    }
}
