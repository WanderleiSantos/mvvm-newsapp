package com.manodev.mvvmnewsapp.core.domain

data class NewsList(
    val nextPage: String?,
    val results: List<Article>
)
