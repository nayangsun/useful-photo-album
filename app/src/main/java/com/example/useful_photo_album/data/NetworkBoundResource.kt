package com.example.useful_photo_album.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.useful_photo_album.AppExecutors
import com.example.useful_photo_album.data.remote.Resource

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingTHis")
        val dbSource = loadFromDb()

        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        /**
         * 사실 api의 response를 원하는 값만 db에 저장을 한 뒤
         * 저장한 db를 빼오도록 하는 독립적으로 만들어야 하나
         * 지금은 단순히 api 테스트를 위해서 임의로 설정함.
         */

        val apiResponse = createCallTest()
        /**
         * 궁금한 부분. fetchFromNextwork가 db가 null 일때 들어오는데,
         * 다른 동작에 의해 null이 아닐 경우가 생기는 것일까?
         * 혹시 모르니 최신값을 뿌려주도록 이렇게 열어두는게 좋을까??
         */
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            /**
             * 나중에 retrofit Api response 값에 따라 동작 하도록 감쌀 예정
             */
            appExecutors.diskIO().execute {
                //saveCallResult(response)
                appExecutors.mainThread().execute {
                    setValue(Resource.success(response))
//                    result.addSource(loadFromDb()) { newData ->
//                        setValue(Resource.success(newData))
//                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

//    @WorkerThread
//    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

//    @MainThread
//    protected abstract fun createCall(): LiveData<RequestType>

    @MainThread
    protected abstract fun createCallTest(): LiveData<ResultType>
}