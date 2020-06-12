package com.example.githubproject

import androidx.multidex.MultiDexApplication
import com.example.githubproject.appModule
import org.koin.core.context.startKoin

class Application:  MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(appModule))
        }
    }
}