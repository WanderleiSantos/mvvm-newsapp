package com.manodev.mvvmnewsapp.news.presentation

import com.manodev.mvvmnewsapp.core.domain.Article

data class NewsState(
    val articleList: List<Article> = emptyList(),
    val nextPage: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
