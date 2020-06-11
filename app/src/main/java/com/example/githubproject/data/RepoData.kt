package com.example.githubproject.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.githubproject.manager.NetworkState

data class RepoData(
    val pagedList: LiveData<PagedList<User>>,

    val networkState: LiveData<NetworkState>,

    val initLoadState: LiveData<NetworkState>,

    val retry: () -> Unit
)