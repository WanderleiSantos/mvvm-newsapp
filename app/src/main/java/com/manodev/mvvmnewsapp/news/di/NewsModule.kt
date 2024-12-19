package com.manodev.mvvmnewsapp.news.di

import com.manodev.mvvmnewsapp.news.presentation.NewsViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val newsModule = module {
    viewModel { NewsViewModel(get()) }
}