package com.codelixir.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("/users/{user}/repos")
    suspend fun retrieveRepositories(@Path("user") user: String): List<GitHubData>

    @GET("/users/{user}/repos")
    suspend fun retrieveRepositoriesResponse(@Path("user") user: String): Response<List<GitHubData>>
}