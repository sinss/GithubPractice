package com.example.githubproject.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.githubproject.data.User
import com.example.githubproject.manager.API
import com.example.githubproject.manager.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException
import java.io.IOException

class PageUserData(
    private val pageSize: Int,
    private val api: API,
    private val comp: CompositeDisposable
) : PageKeyedDataSource<Int, User>() {
    val state = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    private var retry: (() -> Any)? = null

    fun executeRetry() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            api.networkExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        initialLoad.postValue(NetworkState.LOADING)
        try {
            comp.add(api.getUsers(perPage = pageSize)
                .doOnError {
                    when (it) {
                        is HttpException -> {
                            initialLoad.postValue(NetworkState.error("error code: ${it.code()}"))
                            state.postValue(NetworkState.error("error code: ${it.code()}"))
                            retry = {
                                loadInitial(params, callback)
                            }
                        }
                    }
                }.subscribeBy(
                    onSuccess = { cells ->

                        val nextId = if (cells.isNotEmpty()) {
                            cells[cells.lastIndex].id
                        } else {
                            0
                        }
                        callback.onResult(cells, null, nextId)
                        retry = null
                        state.postValue(NetworkState.LOADED)
                        initialLoad.postValue(NetworkState.LOADED)
                    },
                    onError = {
                    }
                ))
        } catch (ioException: IOException) {
            initialLoad.postValue(NetworkState.error(ioException.message))
            state.postValue(NetworkState.error(ioException.message))
            retry = {
                loadInitial(params, callback)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {
        val id = params.key
        state.postValue(NetworkState.LOADING)
        try {
            comp.add(api.getUsers(since = id, perPage = pageSize)
                .doOnError {
                    when (it) {
                        is HttpException -> {
                            state.postValue(NetworkState.error("error code: ${it.code()}"))
                            retry = {
                                loadAfter(params, callback)
                            }
                        }
                    }
                }.subscribeBy(
                    onSuccess = { cells ->
                        val nextId = if (cells.isNotEmpty()) {
                            cells[cells.lastIndex].id
                        } else {
                            0
                        }
                        callback.onResult(cells, nextId)
                        retry = null
                        state.postValue(NetworkState.LOADED)
                    },
                    onError = {
                    }
                ))
        } catch (ioException: IOException) {
            state.postValue(NetworkState.error(ioException.message))
            retry = {
                loadAfter(params, callback)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {
        // no need
    }
}