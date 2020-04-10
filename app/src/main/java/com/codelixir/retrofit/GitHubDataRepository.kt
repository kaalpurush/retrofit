package com.codelixir.retrofit

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

object GitHubDataRepository {
    private var data: List<GitHubData>? = null

    suspend fun fetchRepositories(): List<GitHubData>? {
        val service = RetrofitClient.get().create(GitHubService::class.java)
        return try {
            service.retrieveRepositories("kaalpurush")
        } catch (e: Exception) {
            null
        }

    }

    suspend fun getRepositories(refresh: Boolean = false): List<GitHubData>? {
        if (!refresh && data !== null)
            return data

        data = fetchRepositories()
        return data
    }

    interface GitHubService {
        @GET("/users/{user}/repos")
        suspend fun retrieveRepositories(@Path("user") user: String): List<GitHubData>

        @GET("/users/{user}/repos")
        suspend fun retrieveRepositoriesResponse(@Path("user") user: String): Response<List<GitHubData>>
    }
}