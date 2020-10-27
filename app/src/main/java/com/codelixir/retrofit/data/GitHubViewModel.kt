package com.codelixir.retrofit.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class GitHubViewModel(application: Application) : AndroidViewModel(application) {

    private var data: LiveData<List<GitHubEntity>>

    init {
        data = liveData {
            GitHubDataRepository.fetchRepositories()?.let {
                emit(it)
            }
        }
    }

    fun getRepositories(refresh: Boolean = false): LiveData<List<GitHubEntity>> {
        if (refresh)
            data = liveData {
                GitHubDataRepository.fetchRepositories()?.let {
                    emit(it)
                }
            }
        return data
    }

    fun getSharedRepositories(refresh: Boolean = false): LiveData<List<GitHubEntity>> {
        return liveData {
            GitHubDataRepository.getRepositories(refresh)?.let {
                emit(it)
            }
        }
    }
}