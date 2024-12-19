package com.manodev.mvvmnewsapp

import android.app.Application
import com.manodev.mvvmnewsapp.core.di.coreModule
import com.manodev.mvvmnewsapp.news.di.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                newsModule
            )
        }
    }
}