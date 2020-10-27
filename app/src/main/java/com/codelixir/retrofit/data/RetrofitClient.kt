package com.codelixir.retrofit.data

import com.codelixir.retrofit.Application
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
            //addConverterFactory(GsonConverterFactory.create())
            build()
        }
    }

    fun get(): Retrofit {
        return client
    }

    inline fun <reified T> createCaller(): T {
        return get().create(T::class.java)
    }
}
