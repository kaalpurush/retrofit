package com.codelixir.retrofit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class GitHubViewModel(application: Application) : AndroidViewModel(application) {

    private var data: LiveData<List<GitHubData>>

    init {
        data = fetchRepositories()
    }

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
        if (refresh)
            data = fetchRepositories()
        return data
    }

    fun getSharedRepositories(refresh: Boolean = false): LiveData<List<GitHubData>> {
        return GitHubDataRepository.getRepositories(refresh)
    }
}