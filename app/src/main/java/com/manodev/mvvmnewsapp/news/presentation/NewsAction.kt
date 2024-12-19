package com.manodev.mvvmnewsapp.news.presentation

sealed interface NewsAction {
    data object Paginate: NewsAction
}