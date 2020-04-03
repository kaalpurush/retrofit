package com.codelixir.retrofit

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import retrofit2.HttpException

object GitHubDataRepository {
    lateinit private var data: LiveData<List<GitHubData>>

    fun fetchRepositories(): LiveData<List<GitHubData>> {
        return liveData {
            val service = RetrofitClient.get().create(GitHubService::class.java)
            try {
                var ret = service.retrieveRepositories("kaalpurush")
                emit(ret)
            } catch (e: Exception) {
            }
        }
    }

    fun getRepositories(refresh: Boolean = false): LiveData<List<GitHubData>> {
        if (!refresh && ::data.isInitialized)
            return data

        data = fetchRepositories()
        return data
    }
}