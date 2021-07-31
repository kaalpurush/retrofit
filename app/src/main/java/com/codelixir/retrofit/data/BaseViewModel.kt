package com.codelixir.retrofit.data

import androidx.lifecycle.*
import com.codelixir.retrofit.util.toast
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val progress: MutableLiveData<Boolean> = MutableLiveData()

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

    fun <T : Any> T.processAPI(showProgress: Boolean = true, showError: Boolean = true): T {
        return this
    }


    fun processAPI(block: () -> Unit) {
        toast(com.codelixir.retrofit.Application.context, "pre")
        val response = block.invoke()
        toast(com.codelixir.retrofit.Application.context, "post")
    }
}