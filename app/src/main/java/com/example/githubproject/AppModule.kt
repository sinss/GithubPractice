package com.example.githubproject

import com.example.githubproject.manager.API
import com.example.githubproject.viewModel.GithubUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single {
        API()
    }

    viewModel {
        GithubUserViewModel(get())
    }
}