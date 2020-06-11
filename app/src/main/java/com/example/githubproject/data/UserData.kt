package com.example.githubproject.data

import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.githubproject.manager.API
import io.reactivex.disposables.CompositeDisposable

class UserData(private val api: API,  private val comp: CompositeDisposable) {

    fun getUsers(): RepoData {

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setPrefetchDistance(10)
            .build()

        val factory = UserDataFactory(config.pageSize, api, comp)

        val livePagedList = LivePagedListBuilder(factory, config).build()

        return RepoData(
            pagedList = livePagedList,
            networkState = factory.dataSource.switchMap { it.state },
            retry = { factory.dataSource.value?.executeRetry() },
            initLoadState = factory.dataSource.switchMap { it.initialLoad }
        )
    }
}
