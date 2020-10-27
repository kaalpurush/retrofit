package com.codelixir.retrofit.data

import com.codelixir.retrofit.Application
import com.codelixir.retrofit.util.hasInternet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

object GitHubDataRepository {
    private var data: List<GitHubEntity>? = null

    private val service by lazy {
        RetrofitClient.createCaller<GitHubService>()
    }

    private val db by lazy {
        AppDatabase.getInstance(Application.context)
    }

    suspend fun fetchRepositories(): List<GitHubEntity>? {
        return if (hasInternet(Application.context)) {
            return try {
                val data = service.retrieveRepositories("kaalpurush")
                db.gitHubDao().insertAll(data)
                data
            } catch (e: Exception) {
                null
            }
        } else {
            db.gitHubDao().all()
        }
    }

    suspend fun getRepositories(refresh: Boolean = false): List<GitHubEntity>? {
        if (!refresh && data !== null)
            return data

        data = fetchRepositories()
        return data
    }

    interface GitHubService {
        @GET("/users/{user}/repos")
        suspend fun retrieveRepositories(@Path("user") user: String): List<GitHubEntity>

        @GET("/users/{user}/repos")
        suspend fun retrieveRepositoriesResponse(@Path("user") user: String): Response<List<GitHubEntity>>
    }
}