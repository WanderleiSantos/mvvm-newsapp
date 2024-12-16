package com.manodev.mvvmnewsapp.core.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class NewsListDto(
    val nextPage: String?,
    val results: List<ArticleDto>?
)