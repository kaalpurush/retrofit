package com.codelixir.retrofit.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class GitHubViewModel(application: Application) : BaseViewModel(application) {

    fun getRepositoriesWithFallback(): LiveData<Resource<List<GitHubEntity>?>> =
        callApi(
            apiRequest = { GitHubDataRepository.fetchRepositories() },
            dbFetchFunc = { GitHubDataRepository.fetchRepositoriesFromDB() },
            dbInsertFunc = { list -> GitHubDataRepository.insertRepositories(list) }
        )


    fun getRepositories(): LiveData<Resource<List<GitHubEntity>>> =
        liveData {
            emit(Resource.loading<List<GitHubEntity>>(null))
            GitHubDataRepository.fetchRepositories().apply { emit(Resource.data(this)) }
        }

}