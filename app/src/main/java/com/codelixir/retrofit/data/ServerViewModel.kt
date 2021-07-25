package com.codelixir.retrofit.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val service: ServerService
) : BaseViewModel() {
    fun getList(): LiveData<Resource<List<ServerEntity>>> {
        return liveData {
            emit(Resource.data(service.getList()))
        }
    }
}