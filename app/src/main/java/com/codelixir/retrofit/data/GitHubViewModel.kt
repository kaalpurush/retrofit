package com.codelixir.retrofit.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class GitHubViewModel : BaseViewModel() {

    private lateinit var data: LiveData<Resource<List<GitHubEntity>>>

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

    fun getSharedRepositories(refresh: Boolean = false): LiveData<Resource<List<GitHubEntity>>> {
        if (!refresh && this::data.isInitialized) return data

        data = liveData {
            emit(Resource.loading<List<GitHubEntity>>(null))
            GitHubDataRepository.getRepositories(refresh).apply { emit(Resource.data(this)) }
        }
        return data;
    }

}