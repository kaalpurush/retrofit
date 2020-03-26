package com.codelixir.retrofit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class GitHubViewModel(application: Application) : AndroidViewModel(application) {

    private var data: LiveData<List<GitHubRepositories>>

    init {
        data = fetchRepositories()
    }

    fun fetchRepositories(): LiveData<List<GitHubRepositories>> {
        return liveData {
            val service = RetrofitClient.get().create(GitHubService::class.java)
            emit(service.retrieveRepositories("kaalpurush"))
        }
    }

    fun getRepositories(): LiveData<List<GitHubRepositories>> {
        return data
    }
}