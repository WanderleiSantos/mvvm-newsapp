package com.manodev.mvvmnewsapp.core.domain

data class NewsList(
    val nextPage: String?,
    val articles: List<Article>
)
