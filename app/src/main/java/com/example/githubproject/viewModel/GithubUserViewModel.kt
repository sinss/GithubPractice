package com.example.githubproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.githubproject.data.RepoData
import com.example.githubproject.data.GithubUser
import com.example.githubproject.data.UserDataFetcher
import com.example.githubproject.manager.API
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class GithubUserViewModel(private val api: API): ViewModel() {

    private val disposables = CompositeDisposable()

    private val userRepo = UserDataFetcher(api, disposables)

    private val repoResult = MutableLiveData<RepoData>()

    private val _focusUser = MutableLiveData<GithubUser>()

    val focusGithubUser: LiveData<GithubUser> = _focusUser

    val pagedList = repoResult.switchMap { it.pagedList }
    val networkState = repoResult.switchMap { it.networkState }
    val initLoadState = repoResult.switchMap { it.initLoadState }

    fun rxLaunch(job: () -> Disposable?) {
        job()?.let { disposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getGitHubUsers() {
        repoResult.postValue(userRepo.getUsers())
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }

    fun onClickUserInfo(userId: String) {
        rxLaunch {
            api.getUser(userId).subscribeBy(
                onSuccess = { user ->
                    _focusUser.postValue(user)
                },
                onError = {
                }
            )
        }
    }
}