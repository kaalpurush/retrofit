package com.codelixir.retrofit.data

import android.util.Log
import com.codelixir.retrofit.Application
import com.codelixir.retrofit.util.hasInternet
import com.codelixir.retrofit.util.runIfConnected
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception

object GitHubDataRepository {
    private var data: List<GitHubEntity>? = null

    private val service by lazy {
        RetrofitClient.createCaller<GitHubService>()
    }

    private val db by lazy {
        AppDatabase.getInstance(Application.context)
    }

    suspend fun fetchRepositories(): List<GitHubEntity>? {
        return try {
            service.retrieveRepositories("kaalpurush")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchRepositoriesFromDB(): List<GitHubEntity>? {
        return db.gitHubDao().all()
    }

    suspend fun insertRepositories(data: List<GitHubEntity>?) {
        data?.let {
            db.gitHubDao().insertAll(data)
        }
    }

    suspend fun getRepositories(refresh: Boolean = false): List<GitHubEntity>? {
        if (!refresh && data !== null)
            return data

        return fetchRepositories()
    }

    suspend fun getRepository(name: String): GitHubEntity? {
        return service.retrieveRepository("kaalpurush", name)
    }

    interface GitHubService {
        //https://api.github.com/users/kaalpurush/repos
        @GET("/users/{user}/repos")
        suspend fun retrieveRepositories(@Path("user") user: String): List<GitHubEntity>

        //https://api.github.com/repos/kaalpurush/retrofit-kotlin
        @GET("/repos/{user}/{repo_name}")
        suspend fun retrieveRepository(
            @Path("user") user: String,
            @Path("repo_name") repo_name: String
        ): GitHubEntity
    }
}