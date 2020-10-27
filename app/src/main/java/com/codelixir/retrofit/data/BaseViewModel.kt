package com.codelixir.retrofit.data

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected fun <T> callApi(
        apiRequest: suspend () -> T?,
        dbFetchFunc: (suspend () -> T)?,
        dbInsertFunc: (suspend (T) -> Unit)?,
        receiverLiveData: MediatorLiveData<Resource<T>>? = null
    ): LiveData<Resource<T>> =
        (receiverLiveData ?: MediatorLiveData()).apply {

            postValue(Resource.loading())

            dbFetchFunc?.let {
                val repoLiveData = liveData {
                    emit(dbFetchFunc.invoke())
                }

                addSource(repoLiveData) { t ->
                    setValue(
                        Resource.success(
                            t
                        )
                    )
                }
            }

            val apiLiveData = liveData {
                emit(apiRequest.invoke())
            }

            addSource(apiLiveData) { t ->
                if (t != null && dbInsertFunc != null) {
                    viewModelScope.launch {
                        dbInsertFunc.invoke(t)
                    }
                }

                setValue(
                    Resource.data(
                        t
                    )
                )
            }
        }
}