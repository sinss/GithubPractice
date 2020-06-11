package com.example.githubproject.viewModel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.githubproject.data.RepoData
import com.example.githubproject.data.UserData
import com.example.githubproject.manager.API
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class GithubUserViewModel(private val api: API): ViewModel() {

    private val disposables = CompositeDisposable()

    private val userRepo = UserData(api, disposables)

    private val repoResult = MutableLiveData<RepoData>()

    private val _focusUser = MutableLiveData<UserData>()

    val focusUser: LiveData<UserData> = _focusUser

    val pagedList = repoResult.switchMap { it.pagedList }
    val networkState = repoResult.switchMap { it.networkState }
    val initLoadState = repoResult.switchMap { it.initLoadState }

    fun rxLaunch(job: () -> Disposable?) {
        job()?.let { disposables.add(it) }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}