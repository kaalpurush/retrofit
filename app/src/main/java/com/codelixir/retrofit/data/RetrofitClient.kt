package com.codelixir.retrofit.data

import com.codelixir.retrofit.Application
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client by lazy {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB

        val cache = Cache(Application.context.cacheDir, cacheSize)

        val okHttpClient = OkHttpClient.Builder().run {
            cache(cache)
            build()
        }

        Retrofit.Builder().run {
            client(okHttpClient)
            baseUrl("https://api.github.com/")
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
    }

    fun get(): Retrofit {
        return client
    }
}
