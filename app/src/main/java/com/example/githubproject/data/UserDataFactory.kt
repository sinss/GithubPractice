package com.example.githubproject.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.githubproject.dataSource.PageUserData
import com.example.githubproject.manager.API
import io.reactivex.disposables.CompositeDisposable


class UserDataFactory(private val pageSize: Int, private val api: API, private val comp: CompositeDisposable) : DataSource.Factory<Int, GithubUser>() {

    val dataSource = MutableLiveData<PageUserData>()

    override fun create(): DataSource<Int, GithubUser> {
        return PageUserData(pageSize, api, comp).apply {
            dataSource.postValue(this)
        }
    }
}