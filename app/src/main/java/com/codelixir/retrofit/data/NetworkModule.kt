package com.codelixir.retrofit.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitClient.get()

    @Provides
    @Singleton
    fun provideServerService(retrofit: Retrofit): ServerService {
        return retrofit.create()
        //return ServerMockService()
    }
}